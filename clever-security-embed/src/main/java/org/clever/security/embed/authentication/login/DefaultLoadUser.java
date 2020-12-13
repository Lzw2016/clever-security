package org.clever.security.embed.authentication.login;

import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.exception.UnsupportedLoginTypeException;
import org.clever.security.model.UserInfo;
import org.clever.security.model.login.*;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;

/**
 * 加载用户信息
 * <p>
 * 作者：ymx <br/>
 * 创建时间：2020/12/6 17:23 <br/>
 */
public class DefaultLoadUser implements LoadUser {

    @Override
    public boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserLoginReq loginReq) {
        return true;
    }

    @Override
    public UserInfo loadUserInfo(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserLoginReq loginReq) {
        if (loginReq instanceof LoginNamePasswordReq) {
            return loadUser(securityConfig, request, (LoginNamePasswordReq) loginReq);
        } else if (loginReq instanceof SmsValidateCodeReq) {
            return loadUser(securityConfig, request, (SmsValidateCodeReq) loginReq);
        } else if (loginReq instanceof WechatSmallProgramReq) {
            return loadUser(securityConfig, request, (WechatSmallProgramReq) loginReq);
        } else if (loginReq instanceof EmailValidateCodeReq) {
            return loadUser(securityConfig, request, (EmailValidateCodeReq) loginReq);
        } else if (loginReq instanceof ScanCodeReq) {
            return loadUser(securityConfig, request, (ScanCodeReq) loginReq);
        } else {
            throw new UnsupportedLoginTypeException("不支持的登录类型");
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    protected UserInfo loadUser(SecurityConfig securityConfig, HttpServletRequest request, LoginNamePasswordReq req) {
        // TODO 根据“loginName”和“password”加载用户信息
        return null;
    }

    protected UserInfo loadUser(SecurityConfig securityConfig, HttpServletRequest request, SmsValidateCodeReq req) {
        // TODO 根据“telephone”加载用户信息
        return null;
    }

    protected UserInfo loadUser(SecurityConfig securityConfig, HttpServletRequest request, WechatSmallProgramReq req) {
        // TODO 根据“微信登录code”加载用户信息
        return null;
    }

    protected UserInfo loadUser(SecurityConfig securityConfig, HttpServletRequest request, EmailValidateCodeReq req) {
        // TODO 根据“email”加载用户信息
        return null;
    }

    protected UserInfo loadUser(SecurityConfig securityConfig, HttpServletRequest request, ScanCodeReq req) {
        // TODO 根据“scanCode”加载用户信息
        return null;
    }
}
