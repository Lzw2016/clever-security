package org.clever.security.embed.register;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.common.utils.validator.BaseValidatorUtils;
import org.clever.common.utils.validator.ValidatorFactoryUtils;
import org.clever.security.Constant;
import org.clever.security.client.RegisterSupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.EmailRegisterConfig;
import org.clever.security.embed.config.internal.LoginNameRegisterConfig;
import org.clever.security.embed.config.internal.SmsRegisterConfig;
import org.clever.security.embed.config.internal.UserRegisterConfig;
import org.clever.security.embed.exception.RegisterInnerException;
import org.clever.security.embed.exception.RegisterValidateCodeException;
import org.clever.security.embed.utils.HttpServletRequestUtils;
import org.clever.security.embed.utils.HttpServletResponseUtils;
import org.clever.security.embed.utils.PathFilterUtils;
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
 * 创建时间：2021/01/09 22:21 <br/>
 */
@Slf4j
public class RegisterCaptchaFilter extends HttpFilter {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    /**
     * 注册支持对象
     */
    private final RegisterSupportClient registerSupportClient;

    public RegisterCaptchaFilter(SecurityConfig securityConfig, RegisterSupportClient registerSupportClient) {
        Assert.notNull(securityConfig, "权限系统配置对象(SecurityConfig)不能为null");
        Assert.notNull(registerSupportClient, "参数registerSupportClient不能为null");
        this.securityConfig = securityConfig;
        this.registerSupportClient = registerSupportClient;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean isRegisterCaptchaRequest = false;
        try {
            if (PathFilterUtils.isLoginNameRegisterCaptchaRequest(request, securityConfig)
                    && securityConfig.getRegister().getLoginNameRegister().isEnable()
                    && securityConfig.getRegister().getLoginNameRegister().isNeedCaptcha()) {
                // 登录名注册-验证码
                isRegisterCaptchaRequest = true;
                sendLoginNameRegisterCaptcha(response);
            } else if (PathFilterUtils.isSmsRegisterCaptchaRequest(request, securityConfig)
                    && securityConfig.getRegister().getSmsRegister().isEnable()
                    && securityConfig.getRegister().getSmsRegister().isNeedCaptcha()) {
                // 短信注册-图片验证码
                isRegisterCaptchaRequest = true;
                sendSmsRegisterCaptcha(response);
            } else if (PathFilterUtils.isSmsRegisterSmsValidateCodeRequest(request, securityConfig)
                    && securityConfig.getRegister().getSmsRegister().isEnable()) {
                // 短信注册-短信验证码
                isRegisterCaptchaRequest = true;
                sendSmsValidateCode(request, response);
            } else if (PathFilterUtils.isEmailRegisterCaptchaRequest(request, securityConfig)
                    && securityConfig.getRegister().getEmailRegister().isEnable()
                    && securityConfig.getRegister().getEmailRegister().isNeedCaptcha()) {
                // 邮箱注册-图片验证码
                isRegisterCaptchaRequest = true;
                sendEmailRegisterCaptcha(response);
            } else if (PathFilterUtils.isEmailRegisterEmailValidateCodeRequest(request, securityConfig)
                    && securityConfig.getRegister().getEmailRegister().isEnable()) {
                // 邮箱注册-邮箱验证码
                isRegisterCaptchaRequest = true;
                sendEmailValidateCode(request, response);
            }
        } catch (Exception e) {
            isRegisterCaptchaRequest = true;
            log.error("获取注册验证码失败", e);
            HttpServletResponseUtils.sendJson(request, response, HttpServletResponseUtils.getHttpStatus(e), e);
        }
        if (isRegisterCaptchaRequest) {
            return;
        }
        // 不是获取注册验证码相关请求
        chain.doFilter(request, response);
    }

    protected void sendLoginNameRegisterCaptcha(HttpServletResponse response) throws IOException {
        UserRegisterConfig register = securityConfig.getRegister();
        LoginNameRegisterConfig loginNameRegister = register.getLoginNameRegister();
        if (!loginNameRegister.isEnable()) {
            throw new UnsupportedOperationException("未启用登录名注册");
        }
        if (!loginNameRegister.isNeedCaptcha()) {
            throw new UnsupportedOperationException("登录名注册不需要验证码");
        }
        GetLoginNameRegisterCaptchaReq req = new GetLoginNameRegisterCaptchaReq(securityConfig.getDomainId());
        req.setEffectiveTimeMilli((int) loginNameRegister.getEffectiveTime().toMillis());
        GetLoginNameRegisterCaptchaRes res = registerSupportClient.getLoginNameRegisterCaptcha(req);
        log.debug("登录名注册-验证码 -> [{}] | [{}]", res.getCode(), res.getExpiredTime());
        response.addHeader(Constant.Login_Captcha_Digest_Response_Header, res.getDigest());
        byte[] image = EncodeDecodeUtils.decodeBase64(res.getCodeContent());
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.getOutputStream().write(image);
        response.setStatus(HttpStatus.OK.value());
    }

    protected void sendSmsRegisterCaptcha(HttpServletResponse response) throws IOException {
        UserRegisterConfig register = securityConfig.getRegister();
        SmsRegisterConfig smsRegister = register.getSmsRegister();
        if (!smsRegister.isEnable()) {
            throw new UnsupportedOperationException("未启用短信注册");
        }
        if (!smsRegister.isNeedCaptcha()) {
            throw new UnsupportedOperationException("短信注册不需要图片验证码");
        }
        GetSmsRegisterCaptchaReq req = new GetSmsRegisterCaptchaReq(securityConfig.getDomainId());
        req.setEffectiveTimeMilli((int) smsRegister.getCaptchaEffectiveTime().toMillis());
        GetSmsRegisterCaptchaRes res = registerSupportClient.getSmsRegisterCaptcha(req);
        log.debug("短信注册-图片验证码 -> [{}] | [{}]", res.getCode(), res.getExpiredTime());
        response.addHeader(Constant.Login_Captcha_Digest_Response_Header, res.getDigest());
        byte[] image = EncodeDecodeUtils.decodeBase64(res.getCodeContent());
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.getOutputStream().write(image);
        response.setStatus(HttpStatus.OK.value());
    }

    protected void sendSmsValidateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserRegisterConfig register = securityConfig.getRegister();
        SmsRegisterConfig smsRegister = register.getSmsRegister();
        if (!smsRegister.isEnable()) {
            throw new UnsupportedOperationException("未启用短信注册");
        }
        SendSmsValidateCodeReq req = HttpServletRequestUtils.parseBodyToEntity(request, SendSmsValidateCodeReq.class);
        if (req == null) {
            throw new BusinessException("请求数据解析异常(用户注册发送短信验证码)");
        }
        req.setDomainId(securityConfig.getDomainId());
        req.setEffectiveTimeMilli((int) smsRegister.getEffectiveTime().toMillis());
        req.setMaxSendNumInDay(smsRegister.getMaxSendNumInDay());
        try {
            BaseValidatorUtils.validateThrowException(ValidatorFactoryUtils.getValidatorInstance(), req);
        } catch (Exception e) {
            throw new BusinessException("请求数据校验失败(用户注册发送短信验证码)", e);
        }
        // 校验图形验证码
        if (smsRegister.isNeedCaptcha()) {
            if (StringUtils.isBlank(req.getCaptcha()) || StringUtils.isBlank(req.getCaptchaDigest())) {
                throw new BusinessException("验证码不能为空");
            }
            VerifySmsRegisterCaptchaReq verifySmsRegisterCaptchaReq = new VerifySmsRegisterCaptchaReq(securityConfig.getDomainId());
            verifySmsRegisterCaptchaReq.setCaptcha(req.getCaptcha());
            verifySmsRegisterCaptchaReq.setCaptchaDigest(req.getCaptchaDigest());
            VerifySmsRegisterCaptchaRes res = registerSupportClient.verifySmsRegisterCaptcha(verifySmsRegisterCaptchaReq);
            if (res == null) {
                throw new RegisterInnerException("验证图片验证码失败");
            } else if (!res.isSuccess()) {
                throw new RegisterValidateCodeException(res.isExpired() ? "图片验证码已失效" : "图片验证码错误");
            }
        }
        // 发送短信验证码
        SendSmsValidateCodeRes res = registerSupportClient.sendSmsValidateCode(req);
        if (res == null) {
            throw new BusinessException("短信验证码发送失败");
        }
        log.debug("短信注册-验证码 -> [{}] | [{}]", res.getCode(), res.getExpiredTime());
        res.setCode("******");
        HttpServletResponseUtils.sendJson(response, res);
    }

    protected void sendEmailRegisterCaptcha(HttpServletResponse response) throws IOException {
        UserRegisterConfig register = securityConfig.getRegister();
        EmailRegisterConfig emailRegister = register.getEmailRegister();
        if (!emailRegister.isEnable()) {
            throw new UnsupportedOperationException("未启用邮箱注册");
        }
        if (!emailRegister.isNeedCaptcha()) {
            throw new UnsupportedOperationException("邮箱注册不需要图片验证码");
        }
        GetEmailRegisterCaptchaReq req = new GetEmailRegisterCaptchaReq(securityConfig.getDomainId());
        req.setEffectiveTimeMilli((int) emailRegister.getCaptchaEffectiveTime().toMillis());
        GetEmailRegisterCaptchaRes res = registerSupportClient.getEmailRegisterCaptcha(req);
        log.debug("邮箱注册-图片验证码 -> [{}] | [{}]", res.getCode(), res.getExpiredTime());
        response.addHeader(Constant.Login_Captcha_Digest_Response_Header, res.getDigest());
        byte[] image = EncodeDecodeUtils.decodeBase64(res.getCodeContent());
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.getOutputStream().write(image);
        response.setStatus(HttpStatus.OK.value());
    }

    protected void sendEmailValidateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserRegisterConfig register = securityConfig.getRegister();
        EmailRegisterConfig emailRegister = register.getEmailRegister();
        if (!emailRegister.isEnable()) {
            throw new UnsupportedOperationException("未启用邮箱注册");
        }
        SendEmailValidateCodeReq req = HttpServletRequestUtils.parseBodyToEntity(request, SendEmailValidateCodeReq.class);
        if (req == null) {
            throw new BusinessException("请求数据解析异常(发送邮箱验证码)");
        }
        req.setDomainId(securityConfig.getDomainId());
        req.setEffectiveTimeMilli((int) emailRegister.getEffectiveTime().toMillis());
        req.setMaxSendNumInDay(emailRegister.getMaxSendNumInDay());
        try {
            BaseValidatorUtils.validateThrowException(ValidatorFactoryUtils.getValidatorInstance(), req);
        } catch (Exception e) {
            throw new BusinessException("请求数据校验失败(发送邮箱验证码)", e);
        }
        // 校验图形验证码
        if (emailRegister.isNeedCaptcha()) {
            if (StringUtils.isBlank(req.getCaptcha()) || StringUtils.isBlank(req.getCaptchaDigest())) {
                throw new BusinessException("验证码不能为空");
            }
            VerifyEmailRegisterCaptchaReq verifyEmailRegisterCaptchaReq = new VerifyEmailRegisterCaptchaReq(securityConfig.getDomainId());
            verifyEmailRegisterCaptchaReq.setCaptcha(req.getCaptcha());
            verifyEmailRegisterCaptchaReq.setCaptchaDigest(req.getCaptchaDigest());
            VerifyEmailRegisterCaptchaRes res = registerSupportClient.verifyEmailRegisterCaptcha(verifyEmailRegisterCaptchaReq);
            if (res == null) {
                throw new RegisterInnerException("验证图片验证码失败");
            } else if (!res.isSuccess()) {
                throw new RegisterValidateCodeException(res.isExpired() ? "图片验证码已失效" : "图片验证码错误");
            }
        }
        // 发送短信验证码
        SendEmailValidateCodeRes res = registerSupportClient.sendEmailValidateCode(req);
        if (res == null) {
            throw new BusinessException("邮箱验证码发送失败");
        }
        log.debug("邮箱注册-邮箱验证码 -> [{}] | [{}]", res.getCode(), res.getExpiredTime());
        res.setCode("******");
        HttpServletResponseUtils.sendJson(response, res);
    }
}
