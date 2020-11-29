package org.clever.security.embed.authentication;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.CookieUtils;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.TokenConfig;
import org.clever.security.embed.context.ISecurityContextRepository;
import org.clever.security.embed.context.SecurityContextHolder;
import org.clever.security.model.SecurityContext;
import org.springframework.util.Assert;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 21:27 <br/>
 */
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    /**
     * 加载用户信息
     */
    private final ISecurityContextRepository securityContextRepository;

    public AuthenticationInterceptor(
            SecurityConfig securityConfig,
            ISecurityContextRepository securityContextRepository) {
        Assert.notNull(securityConfig, "系统授权配置对象(SecurityConfig)不能为null");
        Assert.notNull(securityContextRepository, "参数securityContextRepository不能null");
        this.securityConfig = securityConfig;
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nullable Object handler) {
        try {
            authentication(request, response);
        } catch (Exception e) {
            // TODO
        }
        return true;
    }

    protected void authentication(HttpServletRequest request, HttpServletResponse response) {
        // 用户登录身份认证
        TokenConfig tokenConfig = securityConfig.getTokenConfig();
        // 1.获取JWT-Token
        String jwtToken = CookieUtils.getCookie(request, tokenConfig.getJwtTokenName());
        // TODO 解析Token

        // 2.根据JWT-Token获取SecurityContext
        SecurityContext securityContext = securityContextRepository.loadContext(jwtToken, request, response);


        // TODO 是否需要存储 SecurityContext ??
        SecurityContextHolder.setSecurityContext(securityContext);
    }

    // TODO 放在 GenericFilterBean 中
    @Override
    public void postHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nullable Object handler, ModelAndView modelAndView) {
        SecurityContextHolder.removeSecurityContext();
    }

    // TODO 放在 GenericFilterBean 中
    @Override
    public void afterCompletion(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nullable Object handler, Exception ex) {
        SecurityContextHolder.removeSecurityContext();
    }
}
