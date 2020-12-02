package org.clever.security.embed.authentication;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.CookieUtils;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.TokenConfig;
import org.clever.security.embed.context.ISecurityContextRepository;
import org.clever.security.embed.context.SecurityContextHolder;
import org.clever.security.embed.exception.AuthenticationException;
import org.clever.security.embed.utils.HttpServletResponseUtils;
import org.clever.security.embed.utils.JwtTokenUtils;
import org.clever.security.model.SecurityContext;
import org.clever.security.model.login.LoginRes;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 21:27 <br/>
 */
@Slf4j
public class AuthenticationFilter extends GenericFilterBean {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    /**
     * 加载用户信息
     */
    private final ISecurityContextRepository securityContextRepository;

    public AuthenticationFilter(
            SecurityConfig securityConfig,
            ISecurityContextRepository securityContextRepository) {
        Assert.notNull(securityConfig, "系统授权配置对象(SecurityConfig)不能为null");
        Assert.notNull(securityContextRepository, "参数securityContextRepository不能null");
        this.securityConfig = securityConfig;
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            log.warn("[clever-security]仅支持HTTP服务器");
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        // TODO 是否需要执行认证逻辑
        // 执行认证逻辑
        try {
            authentication(httpRequest, httpResponse);
            // TODO 登录成功 - 写入SecurityContext
            // SecurityContextHolder.setContext();
        } catch (AuthenticationException e) {
            // 授权失败 TODO -- 授权失败服务器行为策略
            HttpServletResponseUtils.sendJson(httpResponse, LoginRes.loginFailure(e.getMessage()));
        } catch (Throwable e) {
            // 认证异常
            HttpServletResponseUtils.sendJson(httpRequest, httpResponse, HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
        try {
            // 处理业务逻辑
            chain.doFilter(request, response);
        } finally {
            // 清理数据防止内存泄漏
            try {
                clearSecurityContext();
            } catch (Exception e) {
                log.warn("clearSecurityContext失败", e);
            }
        }
    }

    protected void authentication(HttpServletRequest request, HttpServletResponse response) {
        // 用户登录身份认证
        TokenConfig tokenConfig = securityConfig.getTokenConfig();
        // 1.获取JWT-Token
        String jwtToken = CookieUtils.getCookie(request, tokenConfig.getJwtTokenName());
        // 解析Token得到uid
        Claims claims = JwtTokenUtils.parserJwtToken(securityConfig.getTokenConfig(), jwtToken);
        String uid = claims.getSubject();
        // 2.根据JWT-Token获取SecurityContext
        SecurityContext securityContext = securityContextRepository.loadContext(uid, claims, request, response);


        // TODO 是否需要存储 SecurityContext ??
        SecurityContextHolder.setContext(securityContext);
    }

    protected void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }
}
