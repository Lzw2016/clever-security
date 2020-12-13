package org.clever.security.embed.authentication.login;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.utils.validator.ValidatorFactoryUtils;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.dto.request.GetLoginEmailValidateCodeReq;
import org.clever.security.dto.request.GetLoginFailedCountAndCaptchaReq;
import org.clever.security.dto.request.GetLoginSmsValidateCodeReq;
import org.clever.security.dto.request.GetScanCodeLoginInfoReq;
import org.clever.security.dto.response.GetLoginFailedCountAndCaptchaRes;
import org.clever.security.dto.response.GetScanCodeLoginInfoRes;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.LoginCaptchaConfig;
import org.clever.security.embed.config.internal.LoginConfig;
import org.clever.security.embed.exception.LoginDataValidateException;
import org.clever.security.embed.exception.LoginException;
import org.clever.security.embed.exception.LoginValidateCodeException;
import org.clever.security.embed.exception.ScanCodeLoginException;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.ValidateCode;
import org.clever.security.model.login.*;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

/**
 * 默认的登录数据校验实现
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/12/01 20:57 <br/>
 */
@Slf4j
public class DefaultVerifyLoginData implements VerifyLoginData {
    private final LoginSupportClient loginSupportClient;

    public DefaultVerifyLoginData(LoginSupportClient loginSupportClient) {
        Assert.notNull(loginSupportClient, "参数loginSupportClient不能为null");
        this.loginSupportClient = loginSupportClient;
    }

    @Override
    public boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserLoginReq loginReq) {
        return true;
    }

    @Override
    public void verify(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserLoginReq loginReq) throws LoginException {
        if (loginReq == null) {
            throw new LoginDataValidateException("登录数据为空");
        }
        LoginConfig loginConfig = securityConfig.getLogin();
        // 登录数据格式校验(空、长度等)
        verifyLoginData(loginReq);
        // 验证码错误
        verifyLoginCaptcha(securityConfig.getDomainId(), loginConfig, loginReq);
        // 特定的登录方式校验
        if (loginReq instanceof ScanCodeReq) {
            verifyScanCode(securityConfig.getDomainId(), (ScanCodeReq) loginReq);
        } else if (loginReq instanceof SmsValidateCodeReq) {
            verifySmsValidateCode(securityConfig.getDomainId(), (SmsValidateCodeReq) loginReq);
        } else if (loginReq instanceof EmailValidateCodeReq) {
            verifyEmailValidateCode(securityConfig.getDomainId(), (EmailValidateCodeReq) loginReq);
        }
    }

    /**
     * 登录数据格式校验(空、长度等)
     */
    protected void verifyLoginData(AbstractUserLoginReq loginReq) {
        try {
            ValidatorFactoryUtils.getValidatorInstance().validate(loginReq);
        } catch (Exception e) {
            throw new LoginDataValidateException("登录数据校验失败", e);
        }
    }

    /**
     * 登录验证码错误校验
     */
    protected void verifyLoginCaptcha(Long domainId, LoginConfig loginConfig, AbstractUserLoginReq loginReq) {
        LoginCaptchaConfig loginCaptcha = loginConfig.getLoginCaptcha();
        if (loginCaptcha == null || !loginCaptcha.isNeedCaptcha()) {
            return;
        }
        // 获取当前登录失败次数和验证码信息
        GetLoginFailedCountAndCaptchaReq req = new GetLoginFailedCountAndCaptchaReq(domainId);
        req.setCaptchaDigest(loginReq.getCaptchaDigest());
        req.setLoginTypeId(loginReq.getLoginType().getId());
        if (loginReq instanceof LoginNamePasswordReq) {
            req.setLoginUniqueName(((LoginNamePasswordReq) loginReq).getLoginName());
        } else if (loginReq instanceof SmsValidateCodeReq) {
            req.setLoginUniqueName(((SmsValidateCodeReq) loginReq).getTelephone());
        } else if (loginReq instanceof EmailValidateCodeReq) {
            req.setLoginUniqueName(((EmailValidateCodeReq) loginReq).getEmail());
        } else {
            return;
        }
        GetLoginFailedCountAndCaptchaRes res = loginSupportClient.getLoginFailedCountAndCaptcha(req);
        // 不需要验证码
        if (res.getFailedCount() < loginCaptcha.getNeedCaptchaByLoginFailedCount()) {
            return;
        }
        final Date now = new Date();
        if (res.getExpiredTime() != null && now.compareTo(res.getExpiredTime()) >= 0) {
            throw new LoginDataValidateException("验证码已过期");
        }
        if (StringUtils.isBlank(res.getCode())
                || !Objects.equals(res.getDigest(), loginReq.getCaptchaDigest())
                || !Objects.equals(res.getCode(), loginReq.getCaptcha())) {
            throw new LoginDataValidateException("登录验证码错误");
        }
    }

    /**
     * 扫码登录验证
     */
    protected void verifyScanCode(Long domainId, ScanCodeReq scanCodeReq) {
        // 根据扫描二维码获取扫码登录信息
        GetScanCodeLoginInfoReq req = new GetScanCodeLoginInfoReq(domainId);
        req.setScanCode(scanCodeReq.getScanCode());
        GetScanCodeLoginInfoRes res = loginSupportClient.getScanCodeLoginInfo(req);
        if (res == null) {
            throw new ScanCodeLoginException("二维码不存在");
        }
        Date now = new Date();
        if (res.getGetTokenExpiredTime() == null || now.compareTo(res.getGetTokenExpiredTime()) >= 0) {
            throw new ScanCodeLoginException("二维码已过期");
        }
        if (StringUtils.isBlank(res.getBindToken()) || !Objects.equals(res.getScanCodeState(), EnumConstant.ScanCodeLogin_ScanCodeState_2)) {
            throw new ScanCodeLoginException("二维码状态错误");
        }
    }

    /**
     * 手机号验证码登录校验
     */
    protected void verifySmsValidateCode(Long domainId, SmsValidateCodeReq smsValidateCodeReq) {
        // 获取真实发送的手机验证码
        GetLoginSmsValidateCodeReq req = new GetLoginSmsValidateCodeReq(domainId);
        req.setTelephone(smsValidateCodeReq.getTelephone());
        ValidateCode realValidateCode = loginSupportClient.getLoginSmsValidateCode(req);
        verifyValidateCode(realValidateCode, smsValidateCodeReq.getValidateCode());
    }

    /**
     * 邮箱验证码登录校验
     */
    protected void verifyEmailValidateCode(Long domainId, EmailValidateCodeReq emailValidateCodeReq) {
        // 获取真实发送的邮箱验证码
        GetLoginEmailValidateCodeReq req = new GetLoginEmailValidateCodeReq(domainId);
        req.setEmail(emailValidateCodeReq.getEmail());
        ValidateCode realValidateCode = loginSupportClient.getLoginEmailValidateCode(req);
        verifyValidateCode(realValidateCode, emailValidateCodeReq.getValidateCode());
    }

    /**
     * 校验验证码
     *
     * @param realValidateCode 真实的验证码数据
     * @param validateCode     用户提交的验证码
     */
    protected void verifyValidateCode(ValidateCode realValidateCode, String validateCode) {
        if (realValidateCode == null) {
            throw new LoginValidateCodeException("登录验证码不存在");
        }
        Date now = new Date();
        if (realValidateCode.getExpiredTime() == null || now.compareTo(realValidateCode.getExpiredTime()) >= 0) {
            throw new LoginValidateCodeException("登录验证码已过期");
        }
        if (!Objects.equals(realValidateCode.getCode(), validateCode)) {
            throw new LoginValidateCodeException("登录验证码错误");
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
