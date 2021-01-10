package org.clever.security.embed.register;

import org.clever.common.utils.validator.ValidatorFactoryUtils;
import org.clever.security.client.RegisterSupportClient;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.UserRegisterConfig;
import org.clever.security.embed.exception.LoginDataValidateException;
import org.clever.security.embed.exception.RegisterDataValidateException;
import org.clever.security.embed.exception.RegisterException;
import org.clever.security.model.register.AbstractUserRegisterReq;
import org.clever.security.model.register.EmailRegisterReq;
import org.clever.security.model.register.LoginNameRegisterReq;
import org.clever.security.model.register.SmsRegisterReq;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 19:08 <br/>
 */
public class DefaultVerifyRegisterData implements VerifyRegisterData {
    private final RegisterSupportClient registerSupportClient;

    public DefaultVerifyRegisterData(RegisterSupportClient registerSupportClient) {
        Assert.notNull(registerSupportClient, "参数registerSupportClient不能为null");
        this.registerSupportClient = registerSupportClient;
    }

    @Override
    public boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserRegisterReq registerReq) {
        return true;
    }

    @Override
    public void verify(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserRegisterReq registerReq) throws RegisterException {
        if (registerReq == null) {
            throw new RegisterDataValidateException("注册数据为空");
        }
        UserRegisterConfig registerConfig = securityConfig.getRegister();
        // 注册数据格式校验(空、长度等)
        verifyRegisterData(registerReq);
        if (registerReq instanceof LoginNameRegisterReq) {
            verifyLoginNameRegisterCaptcha(securityConfig.getDomainId(), registerConfig, (LoginNameRegisterReq) registerReq);
        } else if (registerReq instanceof SmsRegisterReq) {
            verifySmsValidateCode(securityConfig.getDomainId(), registerConfig, (SmsRegisterReq) registerReq);
        } else if (registerReq instanceof EmailRegisterReq) {
            verifyEmailValidateCode(securityConfig.getDomainId(), registerConfig, (EmailRegisterReq) registerReq);
        }
    }

    /**
     * 注册数据格式校验(空、长度等)
     */
    protected void verifyRegisterData(AbstractUserRegisterReq registerReq) {
        try {
            ValidatorFactoryUtils.getValidatorInstance().validate(registerReq);
        } catch (Exception e) {
            throw new LoginDataValidateException("注册数据校验失败", e);
        }
    }

    /**
     * 验证图片验证码
     */
    protected void verifyLoginNameRegisterCaptcha(Long domainId, UserRegisterConfig registerConfig, LoginNameRegisterReq registerReq) {

    }

    /**
     * 验证短信验证码
     */
    protected void verifySmsValidateCode(Long domainId, UserRegisterConfig registerConfig, SmsRegisterReq registerReq) {

    }

    /**
     * 验证邮箱验证码
     */
    protected void verifyEmailValidateCode(Long domainId, UserRegisterConfig registerConfig, EmailRegisterReq registerReq) {

    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
