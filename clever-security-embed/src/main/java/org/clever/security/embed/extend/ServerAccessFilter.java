package org.clever.security.embed.extend;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.context.SecurityContextHolder;
import org.clever.security.embed.task.CacheServerAccessTokenTask;
import org.clever.security.embed.utils.PathFilterUtils;
import org.clever.security.entity.ServerAccessToken;
import org.springframework.util.Assert;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/23 17:55 <br/>
 */
@Slf4j
public class ServerAccessFilter extends HttpFilter {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    private final CacheServerAccessTokenTask cacheServerAccessTokenTask;

    public ServerAccessFilter(SecurityConfig securityConfig, CacheServerAccessTokenTask cacheServerAccessTokenTask) {
        Assert.notNull(securityConfig, "权限系统配置对象(SecurityConfig)不能为null");
        Assert.notNull(cacheServerAccessTokenTask, "参数cacheServerAccessTokenTask不能为null");
        this.securityConfig = securityConfig;
        this.cacheServerAccessTokenTask = cacheServerAccessTokenTask;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!PathFilterUtils.isAuthenticationRequest(request, securityConfig)) {
            // 不需认证
            innerDoFilter(request, response, chain);
            return;
        }
        if (doAuthentication(request)) {
            innerDoFilter(request, response, chain);
        }
    }

    protected void innerDoFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            // 处理业务逻辑
            chain.doFilter(request, response);
        } finally {
            try {
                // 清理数据防止内存泄漏
                SecurityContextHolder.clearServerAccessToken();
            } catch (Exception e) {
                log.warn("clearSecurityContext失败", e);
            }
        }
    }

    /**
     * 执行用户身份认证
     *
     * @param request 请求对象
     * @return true:认证成功, false:认证失败
     */
    protected boolean doAuthentication(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            if (StringUtils.isBlank(value)) {
                continue;
            }
            List<ServerAccessToken> list = cacheServerAccessTokenTask.getCache().get(name);
            if (list == null || list.isEmpty()) {
                continue;
            }
            for (ServerAccessToken token : list) {
                if (Objects.equals(token.getTokenValue(), value)) {
                    SecurityContextHolder.setServerAccessToken(token);
                    return true;
                }
            }
        }
        return false;
    }
}
