package org.clever.security.embed.authentication.login;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.exception.LoginException;
import org.clever.security.embed.exception.LoginInnerException;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.clever.security.model.login.LoginNamePasswordReq;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录数据基础校验
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/12/01 20:57 <br/>
 */
@Slf4j
public class LoginNameVerifyLoginData implements VerifyLoginData {
    private BasicVerifyLoginData basicVerifyLoginData;

    @Override
    public boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserLoginReq loginReq) {
        return loginReq instanceof LoginNamePasswordReq;
    }

    @Override
    public void verify(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserLoginReq loginReq) throws LoginException {
        // 登录数据格式校验(空、长度等)
        if (loginReq == null) {
            throw new LoginInnerException("登录数据不能为空");
        }
        LoginNamePasswordReq req = (LoginNamePasswordReq) loginReq;
        if (StringUtils.isEmpty(req.getLoginName())) {
            throw new LoginInnerException("登录名不能为空");
        }
        if (StringUtils.isEmpty(req.getPassword())) {
            throw new LoginInnerException("登录密码不能为空");
        }
        basicVerifyLoginData.verify(securityConfig, request, loginReq);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
