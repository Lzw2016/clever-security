package org.clever.security.embed.context;

import io.jsonwebtoken.Claims;
import org.clever.security.embed.authentication.login.LoginContext;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.model.SecurityContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/14 21:29 <br/>
 */
public class DefaultSecurityContextRepository implements SecurityContextRepository {
    @Override
    public void cacheContext(LoginContext context, SecurityConfig securityConfig, HttpServletRequest request, HttpServletResponse response) {
        // TODO 缓存SecurityConfig
    }

    @Override
    public SecurityContext loadContext(String uid, Claims claims, HttpServletRequest request, HttpServletResponse response) {
        // TODO 加载SecurityConfig
        return null;
    }
}
