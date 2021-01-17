package org.clever.security.embed.extend;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.common.utils.validator.ValidatorFactoryUtils;
import org.clever.security.Constant;
import org.clever.security.client.PasswordRecoverySupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.AesKeyConfig;
import org.clever.security.embed.config.internal.PasswordEmailRecoveryConfig;
import org.clever.security.embed.config.internal.PasswordRecoveryConfig;
import org.clever.security.embed.config.internal.PasswordSmsRecoveryConfig;
import org.clever.security.embed.exception.BadCredentialsException;
import org.clever.security.embed.exception.PasswordRecoveryInnerException;
import org.clever.security.embed.exception.PasswordRecoveryValidateCodeException;
import org.clever.security.embed.utils.AesUtils;
import org.clever.security.embed.utils.HttpServletRequestUtils;
import org.clever.security.embed.utils.HttpServletResponseUtils;
import org.clever.security.embed.utils.PathFilterUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 密码找回过滤器
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020-12-16 22:27 <br/>
 */
@Slf4j
public class PasswordRecoveryFilter extends GenericFilterBean {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    private final PasswordRecoverySupportClient passwordRecoverySupportClient;

    public PasswordRecoveryFilter(SecurityConfig securityConfig, PasswordRecoverySupportClient passwordRecoverySupportClient) {
        Assert.notNull(securityConfig, "权限系统配置对象(SecurityConfig)不能为null");
        Assert.notNull(passwordRecoverySupportClient, "参数passwordRecoverySupportClient不能为null");
        this.securityConfig = securityConfig;
        this.passwordRecoverySupportClient = passwordRecoverySupportClient;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            log.warn("[clever-security]仅支持HTTP服务器");
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        boolean enablePasswordRecovery = enablePasswordRecovery();
        boolean isPasswordRecovery = false;
        try {
            if (PathFilterUtils.isPasswordRecoverySmsCaptchaRequest(httpRequest, securityConfig)
                    && securityConfig.getPasswordRecovery().getSmsRecovery().isEnable()
                    && securityConfig.getPasswordRecovery().getSmsRecovery().isNeedCaptcha()) {
                // 密码找回 - 短信图片验证码
                isPasswordRecovery = true;
                getSmsRecoveryCaptcha(httpResponse);
            } else if (enablePasswordRecovery
                    && PathFilterUtils.isPasswordRecoverySmsValidateCodeRequest(httpRequest, securityConfig)
                    && securityConfig.getPasswordRecovery().getSmsRecovery().isEnable()) {
                // 密码找回 - 短信验证码
                isPasswordRecovery = true;
                sendSmsValidateCode(httpRequest, httpResponse);
            } else if (PathFilterUtils.isPasswordRecoveryEmailCaptchaRequest(httpRequest, securityConfig)
                    && securityConfig.getPasswordRecovery().getEmailRecovery().isEnable()
                    && securityConfig.getPasswordRecovery().getEmailRecovery().isNeedCaptcha()) {
                // 密码找回 - 邮箱图片验证码
                isPasswordRecovery = true;
                sendEmailCaptcha(httpResponse);
            } else if (enablePasswordRecovery
                    && PathFilterUtils.isPasswordRecoveryEmailValidateCodeRequest(httpRequest, securityConfig)
                    && securityConfig.getPasswordRecovery().getEmailRecovery().isEnable()) {
                // 密码找回 - 邮箱验证码
                isPasswordRecovery = true;
                sendEmailValidateCode(httpRequest, httpResponse);
            } else if (enablePasswordRecovery && PathFilterUtils.isPasswordRecoveryRequest(httpRequest, securityConfig)) {
                // 密码找回请求
                isPasswordRecovery = true;
                recoveryPassword(httpRequest, httpResponse);
            }
        } catch (Exception e) {
            isPasswordRecovery = true;
            log.error("密码找回处理失败", e);
            HttpServletResponseUtils.sendJson(httpRequest, httpResponse, HttpServletResponseUtils.getHttpStatus(e), e);
        }
        if (isPasswordRecovery) {
            return;
        }
        // 不是密码找回相关请求
        chain.doFilter(request, response);
    }

    /**
     * 是否启用了密码找回功能
     */
    protected boolean enablePasswordRecovery() {
        PasswordRecoveryConfig passwordRecovery = securityConfig.getPasswordRecovery();
        if (passwordRecovery == null) {
            return false;
        }
        PasswordSmsRecoveryConfig smsRecovery = passwordRecovery.getSmsRecovery();
        PasswordEmailRecoveryConfig emailRecovery = passwordRecovery.getEmailRecovery();
        return (smsRecovery != null && smsRecovery.isEnable()) || (emailRecovery != null && emailRecovery.isEnable());
    }

    // 密码找回 - 短信图片验证码
    protected void getSmsRecoveryCaptcha(HttpServletResponse response) throws IOException {
        PasswordRecoveryConfig passwordRecovery = securityConfig.getPasswordRecovery();
        PasswordSmsRecoveryConfig smsRecovery = null;
        if (passwordRecovery != null) {
            smsRecovery = passwordRecovery.getSmsRecovery();
        }
        if (smsRecovery == null || !smsRecovery.isEnable()) {
            throw new UnsupportedOperationException("未启用短信找回密码");
        }
        if (!smsRecovery.isNeedCaptcha()) {
            throw new UnsupportedOperationException("短信找回密码不需要验证码");
        }
        GetSmsRecoveryCaptchaReq req = new GetSmsRecoveryCaptchaReq(securityConfig.getDomainId());
        req.setEffectiveTimeMilli((int) smsRecovery.getCaptchaEffectiveTime().toMillis());
        GetSmsRecoveryCaptchaRes res = passwordRecoverySupportClient.getSmsRecoveryCaptcha(req);
        log.debug("短信找回密码-验证码 -> [{}] | [{}]", res.getCode(), res.getExpiredTime());
        response.addHeader(Constant.Login_Captcha_Digest_Response_Header, res.getDigest());
        byte[] image = EncodeDecodeUtils.decodeBase64(res.getCodeContent());
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.getOutputStream().write(image);
        response.setStatus(HttpStatus.OK.value());
    }

    // 密码找回 - 短信验证码
    protected void sendSmsValidateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PasswordRecoveryConfig passwordRecovery = securityConfig.getPasswordRecovery();
        PasswordSmsRecoveryConfig smsRecovery = null;
        if (passwordRecovery != null) {
            smsRecovery = passwordRecovery.getSmsRecovery();
        }
        if (smsRecovery == null || !smsRecovery.isEnable()) {
            throw new UnsupportedOperationException("未启用短信找回密码");
        }
        SendSmsRecoveryValidateCodeReq req = HttpServletRequestUtils.parseBodyToEntity(request, SendSmsRecoveryValidateCodeReq.class);
        if (req == null) {
            throw new BusinessException("请求数据解析异常(找回密码发送短信验证码)");
        }
        req.setDomainId(securityConfig.getDomainId());
        req.setEffectiveTimeMilli((int) smsRecovery.getEffectiveTime().toMillis());
        req.setMaxSendNumInDay(smsRecovery.getMaxSendNumInDay());
        try {
            ValidatorFactoryUtils.getValidatorInstance().validate(req);
        } catch (Exception e) {
            throw new BusinessException("请求数据校验失败(找回密码发送短信验证码)", e);
        }
        // 校验图形验证码
        if (smsRecovery.isNeedCaptcha()) {
            if (StringUtils.isBlank(req.getCaptcha()) || StringUtils.isBlank(req.getCaptchaDigest())) {
                throw new BusinessException("验证码不能为空");
            }
            VerifySmsRecoveryCaptchaReq verifySmsRecoveryCaptchaReq = new VerifySmsRecoveryCaptchaReq(securityConfig.getDomainId());
            verifySmsRecoveryCaptchaReq.setCaptcha(req.getCaptcha());
            verifySmsRecoveryCaptchaReq.setCaptchaDigest(req.getCaptchaDigest());
            VerifySmsRecoveryCaptchaRes res = passwordRecoverySupportClient.verifySmsRecoveryCaptcha(verifySmsRecoveryCaptchaReq);
            if (res == null) {
                throw new PasswordRecoveryInnerException("验证图片验证码失败");
            } else if (!res.isSuccess()) {
                throw new PasswordRecoveryValidateCodeException(res.isExpired() ? "图片验证码已失效" : "图片验证码错误");
            }
        }
        // 发送短信验证码
        SendSmsRecoveryValidateCodeRes res = passwordRecoverySupportClient.sendSmsRecoveryValidateCode(req);
        if (res == null) {
            throw new BusinessException("短信验证码发送失败");
        }
        log.debug("短信找回密码-验证码 -> [{}] | [{}]", res.getCode(), res.getExpiredTime());
        res.setCode("******");
        HttpServletResponseUtils.sendJson(response, res);
    }

    // 密码找回 - 邮箱图片验证码
    protected void sendEmailCaptcha(HttpServletResponse response) throws IOException {
        PasswordRecoveryConfig passwordRecovery = securityConfig.getPasswordRecovery();
        PasswordEmailRecoveryConfig emailRecovery = null;
        if (passwordRecovery != null) {
            emailRecovery = passwordRecovery.getEmailRecovery();
        }
        if (emailRecovery == null || !emailRecovery.isEnable()) {
            throw new UnsupportedOperationException("未启用邮箱找回密码");
        }
        if (!emailRecovery.isNeedCaptcha()) {
            throw new UnsupportedOperationException("邮箱找回密码不需要验证码");
        }
        GetSmsRecoveryCaptchaReq req = new GetSmsRecoveryCaptchaReq(securityConfig.getDomainId());
        req.setEffectiveTimeMilli((int) emailRecovery.getCaptchaEffectiveTime().toMillis());
        GetSmsRecoveryCaptchaRes res = passwordRecoverySupportClient.getSmsRecoveryCaptcha(req);
        log.debug("邮箱找回密码-验证码 -> [{}] | [{}]", res.getCode(), res.getExpiredTime());
        response.addHeader(Constant.Login_Captcha_Digest_Response_Header, res.getDigest());
        byte[] image = EncodeDecodeUtils.decodeBase64(res.getCodeContent());
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.getOutputStream().write(image);
        response.setStatus(HttpStatus.OK.value());
    }

    // 密码找回 - 邮箱验证码
    protected void sendEmailValidateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PasswordRecoveryConfig passwordRecovery = securityConfig.getPasswordRecovery();
        PasswordEmailRecoveryConfig emailRecovery = null;
        if (passwordRecovery != null) {
            emailRecovery = passwordRecovery.getEmailRecovery();
        }
        if (emailRecovery == null || !emailRecovery.isEnable()) {
            throw new UnsupportedOperationException("未启用邮件找回密码");
        }
        SendEmailRecoveryValidateCodeReq req = HttpServletRequestUtils.parseBodyToEntity(request, SendEmailRecoveryValidateCodeReq.class);
        if (req == null) {
            throw new BusinessException("请求数据解析异常(找回密码发送邮件验证码)");
        }
        req.setDomainId(securityConfig.getDomainId());
        req.setEffectiveTimeMilli((int) emailRecovery.getEffectiveTime().toMillis());
        req.setMaxSendNumInDay(emailRecovery.getMaxSendNumInDay());
        try {
            ValidatorFactoryUtils.getValidatorInstance().validate(req);
        } catch (Exception e) {
            throw new BusinessException("请求数据校验失败(找回密码发送邮件验证码)", e);
        }
        // 校验图形验证码
        if (emailRecovery.isNeedCaptcha()) {
            if (StringUtils.isBlank(req.getCaptcha()) || StringUtils.isBlank(req.getCaptchaDigest())) {
                throw new BusinessException("验证码不能为空");
            }
            VerifyEmailRecoveryCaptchaReq verifyEmailRecoveryCaptchaReq = new VerifyEmailRecoveryCaptchaReq(securityConfig.getDomainId());
            verifyEmailRecoveryCaptchaReq.setCaptcha(req.getCaptcha());
            verifyEmailRecoveryCaptchaReq.setCaptchaDigest(req.getCaptchaDigest());
            VerifyEmailRecoveryCaptchaRes res = passwordRecoverySupportClient.verifyEmailRecoveryCaptcha(verifyEmailRecoveryCaptchaReq);
            if (res == null) {
                throw new PasswordRecoveryInnerException("验证图片验证码失败");
            } else if (!res.isSuccess()) {
                throw new PasswordRecoveryValidateCodeException(res.isExpired() ? "图片验证码已失效" : "图片验证码错误");
            }
        }
        // 发送邮件验证码
        SendEmailRecoveryValidateCodeRes res = passwordRecoverySupportClient.sendEmailRecoveryValidateCode(req);
        if (res == null) {
            throw new BusinessException("邮件验证码发送失败");
        }
        log.debug("邮件找回密码-验证码 -> [{}] | [{}]", res.getCode(), res.getExpiredTime());
        res.setCode("******");
        HttpServletResponseUtils.sendJson(response, res);
    }

    // 找回密码
    protected void recoveryPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RecoveryPasswordReq req = HttpServletRequestUtils.parseBodyToEntity(request, RecoveryPasswordReq.class);
        if (req == null) {
            throw new BusinessException("请求数据解析异常(找回密码)");
        }
        if (StringUtils.isBlank(req.getEmail()) && StringUtils.isBlank(req.getTelephone())) {
            throw new BusinessException("邮箱和手机号不能同时为空");
        }
        try {
            ValidatorFactoryUtils.getValidatorInstance().validate(req);
        } catch (Exception e) {
            throw new BusinessException("请求数据校验失败(找回密码)", e);
        }
        String newPassword = req.getNewPassword();
        AesKeyConfig reqAesKey = securityConfig.getReqAesKey();
        if (reqAesKey != null && reqAesKey.isEnable()) {
            // 解密密码(请求密码加密在客户端)
            try {
                newPassword = AesUtils.decode(reqAesKey.getReqPasswordAesKey(), reqAesKey.getReqPasswordAesIv(), newPassword);
                req.setNewPassword(newPassword);
            } catch (Exception e) {
                throw new BadCredentialsException("新密码需要加密传输", e);
            }
        }
        String uid;
        if (StringUtils.isNotBlank(req.getTelephone())) {
            // 验证短信验证码
            VerifySmsRecoveryValidateCodeReq verifySmsRecoveryValidateCodeReq = new VerifySmsRecoveryValidateCodeReq(securityConfig.getDomainId());
            verifySmsRecoveryValidateCodeReq.setCode(req.getCode());
            verifySmsRecoveryValidateCodeReq.setCodeDigest(req.getCodeDigest());
            verifySmsRecoveryValidateCodeReq.setTelephone(req.getTelephone());
            VerifySmsRecoveryValidateCodeRes res = passwordRecoverySupportClient.verifySmsRecoveryValidateCode(verifySmsRecoveryValidateCodeReq);
            if (res == null) {
                throw new PasswordRecoveryInnerException("验证短信验证码失败");
            } else if (!res.isSuccess()) {
                throw new PasswordRecoveryValidateCodeException(res.isExpired() ? "短信验证码已失效" : "短信验证码错误");
            }
            uid = res.getUid();
        } else if (StringUtils.isNotBlank(req.getEmail())) {
            // 验证邮箱验证码
            VerifyEmailRecoveryValidateCodeReq verifyEmailRecoveryValidateCodeReq = new VerifyEmailRecoveryValidateCodeReq(securityConfig.getDomainId());
            verifyEmailRecoveryValidateCodeReq.setCode(req.getCode());
            verifyEmailRecoveryValidateCodeReq.setCodeDigest(req.getCodeDigest());
            verifyEmailRecoveryValidateCodeReq.setEmail(req.getEmail());
            VerifyEmailRecoveryValidateCodeRes res = passwordRecoverySupportClient.verifyEmailRecoveryValidateCode(verifyEmailRecoveryValidateCodeReq);
            if (res == null) {
                throw new PasswordRecoveryInnerException("验证邮件验证码失败");
            } else if (!res.isSuccess()) {
                throw new PasswordRecoveryValidateCodeException(res.isExpired() ? "邮件验证码已失效" : "邮件验证码错误");
            }
            uid = res.getUid();
        } else {
            throw new BusinessException("邮箱和手机号不能同时为空");
        }
        // 重置密码
        ResetPasswordReq resetPasswordReq = new ResetPasswordReq(securityConfig.getDomainId());
        resetPasswordReq.setUid(uid);
        resetPasswordReq.setNewPassword(newPassword);
        ResetPasswordReqRes res = passwordRecoverySupportClient.resetPassword(resetPasswordReq);
        if (res == null) {
            throw new PasswordRecoveryInnerException("重置密码失败");
        } else if (!res.isSuccess()) {
            throw new BusinessException(res.getMessage());
        }
        HttpServletResponseUtils.sendJson(response, res);
    }
}
