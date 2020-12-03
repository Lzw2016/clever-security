package org.clever.security.embed.authentication.login;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.exception.LoginException;
import org.clever.security.model.UserInfo;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/01 21:43 <br/>
 */
@Slf4j
public class BasicVerifyUserInfo implements VerifyUserInfo {
    @Override
    public boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserLoginReq loginReq, UserInfo userInfo) {
        return false;
    }

    @Override
    public void verify(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserLoginReq loginReq, UserInfo userInfo) throws LoginException {
        // TODO BasicVerifyUserInfo
        // 登录用户不存在
        // 密码错误
        // 不支持登录域错误
        // 用户禁用错误
        // 用户过期错误
        // 用户状态错误
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
