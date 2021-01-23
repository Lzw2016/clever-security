package org.clever.security.embed.extend;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.common.utils.validator.BaseValidatorUtils;
import org.clever.common.utils.validator.ValidatorFactoryUtils;
import org.clever.security.Constant;
import org.clever.security.client.UpdatePasswordSupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.AesKeyConfig;
import org.clever.security.embed.config.internal.UpdatePasswordConfig;
import org.clever.security.embed.context.SecurityContextHolder;
import org.clever.security.embed.exception.*;
import org.clever.security.embed.utils.AesUtils;
import org.clever.security.embed.utils.HttpServletRequestUtils;
import org.clever.security.embed.utils.HttpServletResponseUtils;
import org.clever.security.embed.utils.PathFilterUtils;
import org.clever.security.model.SecurityContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/18 18:09 <br/>
 */
@Slf4j
public class UpdatePasswordFilter extends HttpFilter {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    private final UpdatePasswordSupportClient updatePasswordSupportClient;

    public UpdatePasswordFilter(SecurityConfig securityConfig, UpdatePasswordSupportClient updatePasswordSupportClient) {
        Assert.notNull(securityConfig, "权限系统配置对象(SecurityConfig)不能为null");
        Assert.notNull(updatePasswordSupportClient, "参数updatePasswordSupportClient不能为null");
        this.securityConfig = securityConfig;
        this.updatePasswordSupportClient = updatePasswordSupportClient;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean changePassword = false;
        UpdatePasswordConfig bindEmail = securityConfig.getUpdatePassword();
        if (bindEmail == null || !bindEmail.isEnable()) {
            throw new UnsupportedOperationException("未启用邮箱换绑");
        }
        try {
            if (PathFilterUtils.isUpdatePasswordCaptchaRequest(request, securityConfig)) {
                //图片验证码
                changePassword = true;
                getUpdatePasswordCaptcha(response);
            } else if (PathFilterUtils.isUpdatePasswordEmailValidateCodeRequest(request, securityConfig)) {
                //邮箱验证码
                changePassword = true;
                sendEmailValidateCode(request, response);
            } else if (PathFilterUtils.isUpdatePasswordSmsValidateCodeRequest(request, securityConfig)) {
                //短信验证码
                changePassword = true;
                sendSmsValidateCode(request, response);
            } else if (PathFilterUtils.isInitPassWordRequest(request, securityConfig)) {
                //初始化密码
                changePassword = true;
                initPassword(request, response);
            } else if (PathFilterUtils.isUpdatePassWordRequest(request, securityConfig)) {
                //修改密码
                changePassword = true;
                updatePassword(request, response);
            }
        } catch (Exception e) {
            changePassword = true;
            log.error("邮箱换绑处理失败", e);
            HttpServletResponseUtils.sendJson(request, response, HttpServletResponseUtils.getHttpStatus(e), e);
        }
        if (changePassword) {
            return;
        }
        chain.doFilter(request, response);
    }

    //获取图片验证码
    protected void getUpdatePasswordCaptcha(HttpServletResponse response) throws IOException {
        GetUpdatePasswordCaptchaReq req = new GetUpdatePasswordCaptchaReq(securityConfig.getDomainId());
        req.setEffectiveTimeMilli((int) securityConfig.getUpdatePassword().getCaptchaEffectiveTime().toMillis());
        GetUpdatePasswordCaptchaRes res = updatePasswordSupportClient.getUpdatePasswordCaptcha(req);
        log.debug("修改密码-验证码 -> [{}] | [{}]", res.getCode(), res.getExpiredTime());
        response.addHeader(Constant.Login_Captcha_Digest_Response_Header, res.getDigest());
        byte[] image = EncodeDecodeUtils.decodeBase64(res.getCodeContent());
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.getOutputStream().write(image);
        response.setStatus(HttpStatus.OK.value());
    }

    //发送手机验证码
    protected void sendSmsValidateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SendSmsUpdatePasswordValidateCodeReq req = HttpServletRequestUtils.parseBodyToEntity(request, SendSmsUpdatePasswordValidateCodeReq.class);
        if (req == null) {
            throw new BusinessException("请求数据解析异常(修改密码短信验证码)");
        }
        req.setDomainId(securityConfig.getDomainId());
        req.setEffectiveTimeMilli((int) securityConfig.getUpdatePassword().getSmsEffectiveTime().toMillis());
        req.setMaxSendNumInDay(securityConfig.getUpdatePassword().getSmsMaxSendNumInDay());
        try {
            BaseValidatorUtils.validateThrowException(ValidatorFactoryUtils.getValidatorInstance(), req);
        } catch (Exception e) {
            throw new BusinessException("请求数据校验失败(修改密码短信验证码)", e);
        }
        VerifyCaptchaValidate(req.getCaptcha(), req.getCaptchaDigest());
        // 发送短信验证码
        SendSmsUpdatePasswordValidateCodeRes res = updatePasswordSupportClient.sendSmsUpdatePasswordValidateCode(req);
        if (res == null) {
            throw new BusinessException("短信验证码发送失败");
        }
        log.debug("修改密码-短信验证码 -> [{}] | [{}]", res.getCode(), res.getExpiredTime());
        res.setCode("******");
        HttpServletResponseUtils.sendJson(response, res);
    }

    //发送邮箱验证码
    protected void sendEmailValidateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SendEmailUpdatePasswordValidateCodeReq req = HttpServletRequestUtils.parseBodyToEntity(request, SendEmailUpdatePasswordValidateCodeReq.class);
        if (req == null) {
            throw new BusinessException("请求数据解析异常(修改密码发送邮箱验证码)");
        }
        req.setDomainId(securityConfig.getDomainId());
        req.setEffectiveTimeMilli((int) securityConfig.getUpdatePassword().getEmailEffectiveTime().toMillis());
        req.setMaxSendNumInDay(securityConfig.getUpdatePassword().getEmailMaxSendNumInDay());
        try {
            BaseValidatorUtils.validateThrowException(ValidatorFactoryUtils.getValidatorInstance(), req);
        } catch (Exception e) {
            throw new BusinessException("请求数据校验失败(修改密码发送邮箱验证码)", e);
        }
        VerifyCaptchaValidate(req.getCaptcha(), req.getCaptchaDigest());
        // 发送邮箱验证码
        SendEmailUpdatePasswordValidateCodeRes res = updatePasswordSupportClient.sendEmailUpdatePasswordValidateCode(req);
        if (res == null) {
            throw new BusinessException("邮箱验证码发送失败");
        }
        log.debug("修改密码-邮箱验证码 -> [{}] | [{}]", res.getCode(), res.getExpiredTime());
        res.setCode("******");
        HttpServletResponseUtils.sendJson(response, res);
    }

    //初始化密码
    protected void initPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityContext securityContext = SecurityContextHolder.getContext(request);
        if (securityContext == null) {
            throw new AuthorizationInnerException("获取SecurityContext失败");
        }
        InitPasswordReq req = HttpServletRequestUtils.parseBodyToEntity(request, InitPasswordReq.class);
        if (req == null) {
            throw new BusinessException("请求数据解析异常(初始化密码)");
        }
        req.setUid(securityContext.getUserInfo().getUid());
        try {
            BaseValidatorUtils.validateThrowException(ValidatorFactoryUtils.getValidatorInstance(), req);
        } catch (Exception e) {
            throw new BusinessException("请求数据校验失败(初始化密码)", e);
        }
        InitPasswordRes res = updatePasswordSupportClient.initPassword(req);
        if (res == null) {
            throw new InitPasswordInnerException("设置密码失败");
        } else if (!res.isSuccess()) {
            throw new BusinessException(res.getMessage());
        }
        HttpServletResponseUtils.sendJson(response, res);
    }

    //修改密码
    protected void updatePassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityContext securityContext = SecurityContextHolder.getContext(request);
        if (securityContext == null) {
            throw new AuthorizationInnerException("获取SecurityContext失败");
        }
        UpdatePasswordReq req = HttpServletRequestUtils.parseBodyToEntity(request, UpdatePasswordReq.class);
        if (req == null) {
            throw new BusinessException("请求数据解析异常(修改密码)");
        }
        if (StringUtils.isBlank(req.getEmail()) && StringUtils.isBlank(req.getTelephone())) {
            throw new BusinessException("邮箱和手机号不能同时为空");
        }
        req.setUid(securityContext.getUserInfo().getUid());
        try {
            BaseValidatorUtils.validateThrowException(ValidatorFactoryUtils.getValidatorInstance(), req);
        } catch (Exception e) {
            throw new BusinessException("请求数据校验失败(修改密码)", e);
        }
        String oldPassword = req.getOldPassword();
        String newPassword = req.getNewPassword();
        AesKeyConfig reqAesKey = securityConfig.getReqAesKey();
        if (reqAesKey != null && reqAesKey.isEnable()) {
            // 解密密码(请求密码加密在客户端)
            try {
                oldPassword = AesUtils.decode(reqAesKey.getReqPasswordAesKey(), reqAesKey.getReqPasswordAesIv(), oldPassword);
                req.setOldPassword(oldPassword);
                newPassword = AesUtils.decode(reqAesKey.getReqPasswordAesKey(), reqAesKey.getReqPasswordAesIv(), newPassword);
                req.setNewPassword(newPassword);
            } catch (Exception e) {
                throw new BadCredentialsException("密码需要加密传输", e);
            }
        }
        if (StringUtils.isNotBlank(req.getTelephone())) {
            VerifySmsValidate(req.getTelephone(), req.getCode(), req.getCodeDigest());
        } else if (StringUtils.isNotBlank(req.getEmail())) {
            VerifyEmailValidate(req.getEmail(), req.getCode(), req.getCodeDigest());
        } else {
            throw new BusinessException("邮箱和手机号不能同时为空");
        }
        //验证短信/邮箱-->验证码
        UpdatePasswordRes res = updatePasswordSupportClient.updatePassword(req);
        if (res == null) {
            throw new InitPasswordInnerException("修改密码失败");
        } else if (!res.isSuccess()) {
            throw new BusinessException(res.getMessage());
        }
        HttpServletResponseUtils.sendJson(response, res);
    }

    // 校验图形验证码
    protected void VerifyCaptchaValidate(String captcha, String captchaDigest) {
        if (securityConfig.getUpdatePassword().isNeedCaptcha()) {
            if (StringUtils.isBlank(captcha) || StringUtils.isBlank(captchaDigest)) {
                throw new BusinessException("验证码不能为空");
            }
            VerifySmsUpdatePasswordValidateCodeReq validateCodeReq = new VerifySmsUpdatePasswordValidateCodeReq(securityConfig.getDomainId());
            validateCodeReq.setCode(captcha);
            validateCodeReq.setCodeDigest(captchaDigest);
            VerifySmsUpdatePasswordValidateCodeRes res = updatePasswordSupportClient.verifySmsUpdatePasswordValidateCode(validateCodeReq);
            if (res == null) {
                throw new UpdatePasswordInnerException("验证图片验证码失败");
            } else if (!res.isSuccess()) {
                throw new UpdatePasswordValidateCodeException(res.isExpired() ? "图片验证码已失效" : "图片验证码错误");
            }
        }
    }

    //验证短信验证码
    protected void VerifySmsValidate(String telephone, String code, String codeDigest) {
        VerifySmsUpdatePasswordValidateCodeReq req = new VerifySmsUpdatePasswordValidateCodeReq(securityConfig.getDomainId());
        req.setCode(code);
        req.setCodeDigest(codeDigest);
        req.setTelephone(telephone);
        VerifySmsUpdatePasswordValidateCodeRes res = updatePasswordSupportClient.verifySmsUpdatePasswordValidateCode(req);
        if (res == null) {
            throw new ChangeBindEmailInnerException("验证短信验证码失败");
        } else if (!res.isSuccess()) {
            throw new ChangeBindEmailValidateCodeException(res.isExpired() ? "短信验证码已失效" : "短信验证码错误");
        }
    }

    //验证邮箱验证码
    protected void VerifyEmailValidate(String email, String code, String codeDigest) {
        VerifyEmailUpdatePasswordValidateCodeReq req = new VerifyEmailUpdatePasswordValidateCodeReq(securityConfig.getDomainId());
        req.setCode(code);
        req.setCodeDigest(codeDigest);
        req.setEmail(email);
        VerifyEmailUpdatePasswordValidateCodeRes res = updatePasswordSupportClient.verifyEmailUpdatePasswordValidateCode(req);
        if (res == null) {
            throw new ChangeBindEmailInnerException("验证邮箱验证码失败");
        } else if (!res.isSuccess()) {
            throw new ChangeBindEmailValidateCodeException(res.isExpired() ? "邮箱验证码已失效" : "邮箱验证码错误");
        }
    }
}
