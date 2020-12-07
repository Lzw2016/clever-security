package org.clever.security.embed.collect;

import org.clever.security.LoginType;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.LoginConfig;
import org.clever.security.embed.utils.HttpServletRequestUtils;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.clever.security.model.login.EmailValidateCodeReq;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 收集“邮箱/邮箱验证码”登录数据
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:05 <br/>
 */
public class EmailValidateCodeReqCollect extends AbstractLoginDataCollect {
    @Override
    public boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request) {
        String loginType = request.getParameter(AbstractUserLoginReq.LoginType_ParamName);
        LoginType loginTypeEnum = LoginType.lookup(loginType);
        if (loginTypeEnum == null) {
            EmailValidateCodeReq req = getEmailValidateCodeReq(securityConfig, request);
            return req != null;
        }
        return Objects.equals(LoginType.Email_ValidateCode.getId(), loginTypeEnum.getId());
    }

    @Override
    public AbstractUserLoginReq collectLoginData(SecurityConfig securityConfig, HttpServletRequest request) {
        return getEmailValidateCodeReq(securityConfig, request);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    protected EmailValidateCodeReq getEmailValidateCodeReq(SecurityConfig securityConfig, HttpServletRequest request) {
        LoginConfig login = securityConfig.getLogin();
        EmailValidateCodeReq req = HttpServletRequestUtils.parseBodyToEntity(request, EmailValidateCodeReq.class);
        if (req == null && login.isPostOnly()) {
            return null;
        }
        if (req == null) {
            req = new EmailValidateCodeReq();
        }
        // 收集基础数据
        collectBaseDataByParameter(req, request);
        // 收集当前登录类型数据
        if (req.getEmail() == null) {
            String email = request.getParameter(EmailValidateCodeReq.Email_ParamName);
            if (email != null) {
                req.setEmail(email);
            }
        }
        if (req.getValidateCode() == null) {
            String validateCode = request.getParameter(EmailValidateCodeReq.ValidateCode_ParamName);
            if (validateCode != null) {
                req.setValidateCode(validateCode);
            }
        }
        if (req.getEmail() == null && req.getValidateCode() == null) {
            return null;
        }
        return req;
    }
}

