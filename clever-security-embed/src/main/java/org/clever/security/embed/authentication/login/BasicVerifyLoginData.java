package org.clever.security.embed.authentication.login;

import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.exception.LoginException;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录数据基础校验
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/12/01 20:57 <br/>
 */
public class BasicVerifyLoginData implements IVerifyLoginData {
    @Override
    public boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserLoginReq loginReq) {
        return true;
    }

    @Override
    public void verify(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserLoginReq loginReq) throws LoginException {
        // TODO BasicVerifyLoginData
        // 登录数据格式校验(空、长度等)
        // 不支持的登录类型
        // 验证码错误
        // 登录数量超过最大并发数量错误
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
