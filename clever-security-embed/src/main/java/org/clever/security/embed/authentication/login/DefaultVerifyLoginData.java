package org.clever.security.embed.authentication.login;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.utils.validator.ValidatorFactoryUtils;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.dto.request.GetLoginEmailValidateCodeReq;
import org.clever.security.dto.request.GetScanCodeLoginInfoReq;
import org.clever.security.dto.request.VerifyLoginCaptchaReq;
import org.clever.security.dto.request.VerifyLoginSmsValidateCodeReq;
import org.clever.security.dto.response.GetScanCodeLoginInfoRes;
import org.clever.security.dto.response.VerifyLoginCaptchaRes;
import org.clever.security.dto.response.VerifyLoginEmailValidateCodeRes;
import org.clever.security.dto.response.VerifyLoginSmsValidateCodeRes;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.LoginCaptchaConfig;
import org.clever.security.embed.config.internal.LoginConfig;
import org.clever.security.embed.exception.*;
import org.clever.security.entity.EnumConstant;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.clever.security.model.login.EmailValidateCodeReq;
import org.clever.security.model.login.ScanCodeReq;
import org.clever.security.model.login.SmsValidateCodeReq;
import org.clever.security.utils.LoginUniqueNameUtils;
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
        VerifyLoginCaptchaReq req = new VerifyLoginCaptchaReq(domainId);
        req.setLoginTypeId(loginReq.getLoginType().getId());
        req.setLoginUniqueName(LoginUniqueNameUtils.getLoginUniqueName(loginReq));
        req.setCaptcha(loginReq.getCaptcha());
        req.setCaptchaDigest(loginReq.getCaptchaDigest());
        req.setNeedCaptchaByLoginFailedCount(loginCaptcha.getNeedCaptchaByLoginFailedCount());
        if (StringUtils.isBlank(req.getLoginUniqueName())) {
            throw new LoginDataValidateException("登录名不能为空");
        }
        VerifyLoginCaptchaRes res = loginSupportClient.verifyLoginCaptcha(req);
        if (res == null) {
            throw new LoginInnerException("验证图片验证码失败");
        } else if (!res.isSuccess()) {
            throw new BadCaptchaException("图片验证码错误");
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
        if (Objects.equals(res.getScanCodeState(), EnumConstant.ScanCodeLogin_ScanCodeState_0)) {
            throw new ScanCodeLoginException("二维码等待扫描");
        }
        if (Objects.equals(res.getScanCodeState(), EnumConstant.ScanCodeLogin_ScanCodeState_1)) {
            throw new ScanCodeLoginException("二维码等待确认");
        }
        if (res.getGetTokenExpiredTime() == null) {
            throw new ScanCodeLoginException("二维码等待扫描和确认");
        }
        Date now = new Date();
        if (now.compareTo(res.getGetTokenExpiredTime()) >= 0) {
            throw new ScanCodeLoginException("二维码已过期");
        }
        if (res.getBindTokenId() == null || !Objects.equals(res.getScanCodeState(), EnumConstant.ScanCodeLogin_ScanCodeState_2)) {
            throw new ScanCodeLoginException("二维码状态错误");
        }
    }

    /**
     * 手机号验证码登录校验
     */
    protected void verifySmsValidateCode(Long domainId, SmsValidateCodeReq smsValidateCodeReq) {
        // 获取真实发送的手机验证码
        VerifyLoginSmsValidateCodeReq req = new VerifyLoginSmsValidateCodeReq(domainId);
        req.setTelephone(smsValidateCodeReq.getTelephone());
        req.setValidateCode(smsValidateCodeReq.getValidateCode());
        req.setValidateCodeDigest(smsValidateCodeReq.getValidateCodeDigest());
        VerifyLoginSmsValidateCodeRes res = loginSupportClient.verifyLoginSmsValidateCode(req);
        if(res==null) {
            throw new LoginInnerException("验证短信验证码失败");
        } else if(!res.isSuccess()) {
            throw new BadCaptchaException("短信验证码错误");
        }
    }

    /**
     * 邮箱验证码登录校验
     */
    protected void verifyEmailValidateCode(Long domainId, EmailValidateCodeReq emailValidateCodeReq) {
        // 获取真实发送的邮箱验证码
        GetLoginEmailValidateCodeReq req = new GetLoginEmailValidateCodeReq(domainId);
        req.setEmail(emailValidateCodeReq.getEmail());
        req.setValidateCode(emailValidateCodeReq.getValidateCode());
        req.setValidateCodeDigest(emailValidateCodeReq.getValidateCodeDigest());
        VerifyLoginEmailValidateCodeRes res = loginSupportClient.verifyLoginEmailValidateCode(req);
        if(res==null) {
            throw new LoginInnerException("验证邮箱验证码失败");
        } else if(!res.isSuccess()) {
            throw new BadCaptchaException("邮箱验证码错误");
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
