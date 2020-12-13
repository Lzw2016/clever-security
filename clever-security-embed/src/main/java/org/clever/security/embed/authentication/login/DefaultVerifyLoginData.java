package org.clever.security.embed.authentication.login;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.utils.validator.ValidatorFactoryUtils;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.LoginCaptchaConfig;
import org.clever.security.embed.config.internal.LoginConfig;
import org.clever.security.embed.exception.*;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.ScanCodeLogin;
import org.clever.security.entity.ValidateCode;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.clever.security.model.login.EmailValidateCodeReq;
import org.clever.security.model.login.ScanCodeReq;
import org.clever.security.model.login.SmsValidateCodeReq;
import org.springframework.core.Ordered;

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
        verifyLoginCaptcha(loginConfig, loginReq);
        // 登录数量超过最大并发数量错误
        verifyConcurrentLoginCount(loginConfig, loginReq);
        // 特定的登录方式校验
        if (loginReq instanceof ScanCodeReq) {
            verifyScanCode((ScanCodeReq) loginReq);
        } else if (loginReq instanceof SmsValidateCodeReq) {
            verifySmsValidateCode((SmsValidateCodeReq) loginReq);
        } else if (loginReq instanceof EmailValidateCodeReq) {
            verifyEmailValidateCode((EmailValidateCodeReq) loginReq);
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
    protected void verifyLoginCaptcha(LoginConfig loginConfig, AbstractUserLoginReq loginReq) {
        LoginCaptchaConfig loginCaptcha =   loginConfig.getLoginCaptcha();
        if (loginCaptcha==null || !loginCaptcha.isNeedCaptcha()) {
            return;
        }
        // TODO 获取当前登录失败次数 和 验证码信息
        int count = 0;
        String realCaptcha = "";
        if (count <= loginCaptcha.getNeedCaptchaByLoginFailedCount()) {
            return;
        }
        if (StringUtils.isBlank(realCaptcha) || !Objects.equals(realCaptcha, loginReq.getCaptcha())) {
            throw new LoginDataValidateException("登录验证码错误");
        }
    }

    /**
     * 登录数量超过最大并发数量校验
     */
    protected void verifyConcurrentLoginCount(LoginConfig loginConfig, AbstractUserLoginReq loginReq) {
        if (loginConfig.getConcurrentLoginCount() <= 0) {
            return;
        }
        if (loginConfig.isAllowAfterLogin()) {
            return;
        }
        // TODO 获取当前用户并发登录数量
        int realConcurrentLoginCount = 0;
        if (realConcurrentLoginCount >= loginConfig.getConcurrentLoginCount()) {
            throw new ConcurrentLoginException("当前用户并发登录次数达到上限");
        }
    }

    /**
     * 扫码登录验证
     */
    protected void verifyScanCode(ScanCodeReq req) {
        // TODO 根据扫描二维码获取扫码登录信息
        ScanCodeLogin scanCodeLogin = null;
        if (scanCodeLogin == null) {
            throw new ScanCodeLoginException("二维码不存在");
        }
        Date now = new Date();
        if (scanCodeLogin.getExpiredTime() == null || now.compareTo(scanCodeLogin.getExpiredTime()) >= 0) {
            throw new ScanCodeLoginException("二维码已过期");
        }
        if (scanCodeLogin.getGetTokenExpiredTime() == null || now.compareTo(scanCodeLogin.getGetTokenExpiredTime()) >= 0) {
            throw new ScanCodeLoginException("二维码已过期");
        }
        if (StringUtils.isBlank(scanCodeLogin.getBindToken()) || !Objects.equals(scanCodeLogin.getScanCodeState(), EnumConstant.ScanCodeLogin_ScanCodeState_2)) {
            throw new ScanCodeLoginException("二维码状态错误");
        }
    }

    /**
     * 手机号验证码登录校验
     */
    protected void verifySmsValidateCode(SmsValidateCodeReq req) {
        // TODO 获取真实发送的手机验证码
        ValidateCode realValidateCode = null;
        verifyValidateCode(realValidateCode, req.getValidateCode());
    }

    /**
     * 邮箱验证码登录校验
     */
    protected void verifyEmailValidateCode(EmailValidateCodeReq req) {
        // TODO 获取真实发送的邮箱验证码
        ValidateCode realValidateCode = null;
        verifyValidateCode(realValidateCode, req.getValidateCode());
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