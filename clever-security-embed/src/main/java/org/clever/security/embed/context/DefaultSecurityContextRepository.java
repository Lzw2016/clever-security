package org.clever.security.embed.context;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.AuthSupportClient;
import org.clever.security.dto.request.CacheContextReq;
import org.clever.security.dto.request.LoadContextReq;
import org.clever.security.embed.authentication.login.LoginContext;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.exception.AuthenticationInnerException;
import org.clever.security.model.SecurityContext;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/14 21:29 <br/>
 */
@Slf4j
public class DefaultSecurityContextRepository implements SecurityContextRepository {
    private final AuthSupportClient authSupportClient;

    public DefaultSecurityContextRepository(AuthSupportClient authSupportClient) {
        Assert.notNull(authSupportClient, "参数authSupportClient不能为null");
        this.authSupportClient = authSupportClient;
    }

    @Override
    public void cacheContext(LoginContext context, SecurityConfig securityConfig, HttpServletRequest request, HttpServletResponse response) {
        // 缓存SecurityConfig
        CacheContextReq req = new CacheContextReq(securityConfig.getDomainId());
        req.setUid(context.getUserInfo().getUid());
        authSupportClient.cacheContext(req);
        log.debug("### 缓存SecurityConfig成功 -> uid={}", req.getUid());
    }

    @Override
    public SecurityContext loadContext(Long domainId, String uid, Claims claims, HttpServletRequest request, HttpServletResponse response) {
        // 加载SecurityConfig
        LoadContextReq req = new LoadContextReq(domainId);
        req.setUid(uid);
        SecurityContext securityContext = authSupportClient.loadContext(req);
        if (securityContext == null) {
            throw new AuthenticationInnerException("加载SecurityConfig失败");
        }
        log.debug("### 加载SecurityConfig成功 -> uid={}", uid);
        return securityContext;
    }
}
