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
        UpdatePasswordConfig updatePassword = securityConfig.getUpdatePassword();
        boolean enableUpdatePassword = updatePassword != null && updatePassword.isEnable();
        boolean isUpdatePassword = false;
        try {
            if (enableUpdatePassword && PathFilterUtils.isInitPasswordCaptchaRequest(request, securityConfig)) {
                // 设置密码-图片验证码
                isUpdatePassword = true;
                getInitPasswordCaptcha(response);
            } else if (enableUpdatePassword && PathFilterUtils.isInitPasswordEmailValidateCodeRequest(request, securityConfig)) {
                // 设置密码-邮箱验证码
                isUpdatePassword = true;
                sendEmailValidateCode(request, response);
            } else if (enableUpdatePassword && PathFilterUtils.isInitPasswordSmsValidateCodeRequest(request, securityConfig)) {
                // 设置密码-手机验证码
                isUpdatePassword = true;
                sendSmsValidateCode(request, response);
            } else if (enableUpdatePassword && PathFilterUtils.isInitPassWordRequest(request, securityConfig)) {
                // 设置密码
                isUpdatePassword = true;
                initPassword(request, response);
            } else if (enableUpdatePassword && PathFilterUtils.isUpdatePassWordRequest(request, securityConfig)) {
                // 修改密码
                isUpdatePassword = true;
                updatePassword(request, response);
            }
        } catch (Exception e) {
            isUpdatePassword = true;
            log.error("密码找回处理失败", e);
            HttpServletResponseUtils.sendJson(request, response, HttpServletResponseUtils.getHttpStatus(e), e);
        }
        if (isUpdatePassword) {
            return;
        }
        // 不是更新密码相关请求
        chain.doFilter(request, response);
    }

    // 获取图片验证码
    protected void getInitPasswordCaptcha(HttpServletResponse response) throws IOException {
        UpdatePasswordConfig updatePassword = securityConfig.getUpdatePassword();
        if (updatePassword == null || !updatePassword.isNeedCaptcha()) {
            throw new UnsupportedOperationException("设置密码不需要图片验证码");
        }
        GetInitPasswordCaptchaReq req = new GetInitPasswordCaptchaReq(securityConfig.getDomainId());
        req.setEffectiveTimeMilli((int) updatePassword.getCaptchaEffectiveTime().toMillis());
        GetInitPasswordCaptchaRes res = updatePasswordSupportClient.getInitPasswordCaptcha(req);
        log.debug("设置密码-图片验证码 -> [{}] | [{}]", res.getCode(), res.getExpiredTime());
        response.addHeader(Constant.Login_Captcha_Digest_Response_Header, res.getDigest());
        byte[] image = EncodeDecodeUtils.decodeBase64(res.getCodeContent());
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.getOutputStream().write(image);
        response.setStatus(HttpStatus.OK.value());
    }

    // 发送手机验证码
    protected void sendSmsValidateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UpdatePasswordConfig updatePassword = securityConfig.getUpdatePassword();
        SendSmsInitPasswordValidateCodeReq req = HttpServletRequestUtils.parseBodyToEntity(request, SendSmsInitPasswordValidateCodeReq.class);
        if (req == null) {
            throw new BusinessException("请求数据解析异常(设置密码短信验证码)");
        }
        req.setDomainId(securityConfig.getDomainId());
        req.setEffectiveTimeMilli((int) updatePassword.getSmsEffectiveTime().toMillis());
        req.setMaxSendNumInDay(updatePassword.getSmsMaxSendNumInDay());
        try {
            BaseValidatorUtils.validateThrowException(ValidatorFactoryUtils.getValidatorInstance(), req);
        } catch (Exception e) {
            throw new BusinessException("请求数据校验失败(设置密码短信验证码)", e);
        }
        verifyCaptchaValidate(req.getCaptcha(), req.getCaptchaDigest());
        // 发送短信验证码
        SendSmsInitPasswordValidateCodeRes res = updatePasswordSupportClient.sendSmsInitPasswordValidateCode(req);
        if (res == null) {
            throw new BusinessException("短信验证码发送失败");
        }
        log.debug("设置密码-短信验证码 -> [{}] | [{}]", res.getCode(), res.getExpiredTime());
        res.setCode("******");
        HttpServletResponseUtils.sendJson(response, res);
    }

    // 发送邮箱验证码
    protected void sendEmailValidateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SendEmailInitPasswordValidateCodeReq req = HttpServletRequestUtils.parseBodyToEntity(request, SendEmailInitPasswordValidateCodeReq.class);
        if (req == null) {
            throw new BusinessException("请求数据解析异常(设置密码发送邮箱验证码)");
        }
        req.setDomainId(securityConfig.getDomainId());
        req.setEffectiveTimeMilli((int) securityConfig.getUpdatePassword().getEmailEffectiveTime().toMillis());
        req.setMaxSendNumInDay(securityConfig.getUpdatePassword().getEmailMaxSendNumInDay());
        try {
            BaseValidatorUtils.validateThrowException(ValidatorFactoryUtils.getValidatorInstance(), req);
        } catch (Exception e) {
            throw new BusinessException("请求数据校验失败(设置密码发送邮箱验证码)", e);
        }
        verifyCaptchaValidate(req.getCaptcha(), req.getCaptchaDigest());
        // 发送邮箱验证码
        SendEmailInitPasswordValidateCodeRes res = updatePasswordSupportClient.sendEmailInitPasswordValidateCode(req);
        if (res == null) {
            throw new BusinessException("邮箱验证码发送失败");
        }
        log.debug("设置密码-邮箱验证码 -> [{}] | [{}]", res.getCode(), res.getExpiredTime());
        res.setCode("******");
        HttpServletResponseUtils.sendJson(response, res);
    }

    // 设置密码
    protected void initPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        InitPasswordReq req = HttpServletRequestUtils.parseBodyToEntity(request, InitPasswordReq.class);
        if (req == null) {
            throw new BusinessException("请求数据解析异常(设置密码)");
        }
        if (StringUtils.isBlank(req.getEmail()) && StringUtils.isBlank(req.getTelephone())) {
            throw new BusinessException("邮箱和手机号不能同时为空");
        }
        try {
            BaseValidatorUtils.validateThrowException(ValidatorFactoryUtils.getValidatorInstance(), req);
        } catch (Exception e) {
            throw new BusinessException("请求数据校验失败(设置密码)", e);
        }
        if (StringUtils.isNotBlank(req.getEmail())) {
            // 验证邮箱验证码
            VerifyEmailInitPasswordValidateCodeReq verifyEmailInitPasswordValidateCodeReq = new VerifyEmailInitPasswordValidateCodeReq(securityConfig.getDomainId());
            verifyEmailInitPasswordValidateCodeReq.setCode(req.getCode());
            verifyEmailInitPasswordValidateCodeReq.setCodeDigest(req.getCodeDigest());
            verifyEmailInitPasswordValidateCodeReq.setEmail(req.getEmail());
            VerifyEmailInitPasswordValidateCodeRes res = updatePasswordSupportClient.verifyEmailInitPasswordValidateCode(verifyEmailInitPasswordValidateCodeReq);
            if (res == null) {
                throw new ChangeBindEmailInnerException("验证邮箱验证码失败");
            } else if (!res.isSuccess()) {
                throw new ChangeBindEmailValidateCodeException(res.isExpired() ? "邮箱验证码已失效" : "邮箱验证码错误");
            }
        } else if (StringUtils.isNotBlank(req.getTelephone())) {
            // 验证短信验证码
            VerifySmsInitPasswordValidateCodeReq verifySmsInitPasswordValidateCodeReq = new VerifySmsInitPasswordValidateCodeReq(securityConfig.getDomainId());
            verifySmsInitPasswordValidateCodeReq.setCode(req.getCode());
            verifySmsInitPasswordValidateCodeReq.setCodeDigest(req.getCodeDigest());
            verifySmsInitPasswordValidateCodeReq.setTelephone(req.getTelephone());
            VerifySmsInitPasswordValidateCodeRes res = updatePasswordSupportClient.verifySmsInitPasswordValidateCode(verifySmsInitPasswordValidateCodeReq);
            if (res == null) {
                throw new ChangeBindEmailInnerException("验证短信验证码失败");
            } else if (!res.isSuccess()) {
                throw new ChangeBindEmailValidateCodeException(res.isExpired() ? "短信验证码已失效" : "短信验证码错误");
            }
        }
        AesKeyConfig reqAesKey = securityConfig.getReqAesKey();
        if (reqAesKey != null && reqAesKey.isEnable()) {
            // 解密密码(请求密码加密在客户端)
            String initPassword = req.getInitPassword();
            try {
                initPassword = AesUtils.decode(reqAesKey.getReqPasswordAesKey(), reqAesKey.getReqPasswordAesIv(), initPassword);
                req.setInitPassword(initPassword);
            } catch (Exception e) {
                throw new BadCredentialsException("密码需要加密传输", e);
            }
        }
        InitPasswordRes res = updatePasswordSupportClient.initPassword(req);
        if (res == null || !res.isSuccess()) {
            throw new InitPasswordInnerException("设置密码失败");
        }
        HttpServletResponseUtils.sendJson(response, res);
    }

    // 修改密码
    protected void updatePassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityContext securityContext = SecurityContextHolder.getContext(request);
        if (securityContext == null) {
            throw new AuthorizationInnerException("获取SecurityContext失败");
        }
        UpdatePasswordReq req = HttpServletRequestUtils.parseBodyToEntity(request, UpdatePasswordReq.class);
        if (req == null) {
            throw new BusinessException("请求数据解析异常(修改密码)");
        }
        req.setUid(securityContext.getUserInfo().getUid());
        try {
            BaseValidatorUtils.validateThrowException(ValidatorFactoryUtils.getValidatorInstance(), req);
        } catch (Exception e) {
            throw new BusinessException("请求数据校验失败(修改密码)", e);
        }
        AesKeyConfig reqAesKey = securityConfig.getReqAesKey();
        if (reqAesKey != null && reqAesKey.isEnable()) {
            // 解密密码(请求密码加密在客户端)
            String oldPassword = req.getOldPassword();
            String newPassword = req.getNewPassword();
            try {
                oldPassword = AesUtils.decode(reqAesKey.getReqPasswordAesKey(), reqAesKey.getReqPasswordAesIv(), oldPassword);
                newPassword = AesUtils.decode(reqAesKey.getReqPasswordAesKey(), reqAesKey.getReqPasswordAesIv(), newPassword);
                req.setOldPassword(oldPassword);
                req.setNewPassword(newPassword);
            } catch (Exception e) {
                throw new BadCredentialsException("密码需要加密传输", e);
            }
        }
        UpdatePasswordRes res = updatePasswordSupportClient.updatePassword(req);
        if (res == null || !res.isSuccess()) {
            throw new InitPasswordInnerException("修改密码失败");
        }
        HttpServletResponseUtils.sendJson(response, res);
    }

    // 校验图形验证码
    protected void verifyCaptchaValidate(String captcha, String captchaDigest) {
        if (!securityConfig.getUpdatePassword().isNeedCaptcha()) {
            return;
        }
        if (StringUtils.isBlank(captcha) || StringUtils.isBlank(captchaDigest)) {
            throw new BusinessException("验证码不能为空");
        }
        VerifyInitPasswordCaptchaReq verifyInitPasswordCaptchaReq = new VerifyInitPasswordCaptchaReq(securityConfig.getDomainId());
        verifyInitPasswordCaptchaReq.setCaptcha(captcha);
        verifyInitPasswordCaptchaReq.setCaptchaDigest(captchaDigest);
        VerifyInitPasswordCaptchaRes res = updatePasswordSupportClient.verifyInitPasswordCaptcha(verifyInitPasswordCaptchaReq);
        if (res == null) {
            throw new UpdatePasswordInnerException("验证图片验证码失败");
        } else if (!res.isSuccess()) {
            throw new UpdatePasswordValidateCodeException(res.isExpired() ? "图片验证码已失效" : "图片验证码错误");
        }
    }
}
