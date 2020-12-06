package org.clever.security.embed.authentication.login;

import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.model.UserInfo;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.clever.security.model.login.LoginNamePasswordReq;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;

/**
 * 加载用户信息
 * <p>
 * 作者：ymx <br/>
 * 创建时间：2020/12/6 17:23 <br/>
 */
public class LoginNameLoadUser implements LoadUser {

    @Override
    public boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserLoginReq loginReq) {
        return loginReq instanceof LoginNamePasswordReq;
    }

    @Override
    public UserInfo loadUserInfo(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserLoginReq loginReq) {
        LoginNamePasswordReq req = (LoginNamePasswordReq) loginReq;
        //todo 根据登录用户名查询所使用的数据库真实用户信息
        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
