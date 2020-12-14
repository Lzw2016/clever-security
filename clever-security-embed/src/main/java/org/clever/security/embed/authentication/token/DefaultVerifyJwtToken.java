package org.clever.security.embed.authentication.token;

import io.jsonwebtoken.Claims;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.exception.AuthenticationException;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/14 21:22 <br/>
 */
public class DefaultVerifyJwtToken implements VerifyJwtToken {
    @Override
    public void verify(String jwtToken, String uid, Claims claims, SecurityConfig securityConfig, HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 校验JWT-Token
        verifyJwtToken();
    }

    protected void verifyJwtToken() {
        // TODO 验证JWT-Token状态
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
