package org.clever.security.embed.authentication;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.utils.CookieUtils;
import org.clever.security.embed.authentication.token.AuthenticationContext;
import org.clever.security.embed.authentication.token.VerifyJwtToken;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.TokenConfig;
import org.clever.security.embed.context.SecurityContextHolder;
import org.clever.security.embed.context.SecurityContextRepository;
import org.clever.security.embed.event.AuthenticationFailureEvent;
import org.clever.security.embed.event.AuthenticationSuccessEvent;
import org.clever.security.embed.exception.AuthenticationException;
import org.clever.security.embed.exception.ParserJwtTokenException;
import org.clever.security.embed.handler.AuthenticationFailureHandler;
import org.clever.security.embed.handler.AuthenticationSuccessHandler;
import org.clever.security.embed.utils.HttpServletResponseUtils;
import org.clever.security.embed.utils.JwtTokenUtils;
import org.clever.security.embed.utils.ListSortUtils;
import org.clever.security.embed.utils.PathFilterUtils;
import org.clever.security.model.SecurityContext;
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
import java.util.List;

/**
 * 用户身份认证拦截器
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 21:27 <br/>
 */
@Slf4j
public class AuthenticationFilter extends GenericFilterBean {
    public final static String JWT_Object_Request_Attribute = AuthenticationFilter.class.getName() + "_JWT_Object";
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    /**
     * JWT-Token验证器
     */
    private final List<VerifyJwtToken> verifyJwtTokenList;
    /**
     * 加载安全上下文(用户信息)
     */
    private final SecurityContextRepository securityContextRepository;
    /**
     * 用户身份认成功处理
     */
    private final List<AuthenticationSuccessHandler> authenticationSuccessHandlerList;
    /**
     * 用户身份认失败处理
     */
    private final List<AuthenticationFailureHandler> authenticationFailureHandlerList;

    public AuthenticationFilter(
            SecurityConfig securityConfig,
            List<VerifyJwtToken> verifyJwtTokenList,
            SecurityContextRepository securityContextRepository,
            List<AuthenticationSuccessHandler> authenticationSuccessHandlerList,
            final List<AuthenticationFailureHandler> authenticationFailureHandlerList) {
        Assert.notNull(securityConfig, "权限系统配置对象(SecurityConfig)不能为null");
        Assert.notEmpty(verifyJwtTokenList, "JWT-Token验证器(VerifyJwtToken)不存在");
        Assert.notNull(securityContextRepository, "安全上下文存取器(SecurityContextRepository)不能为null");
        Assert.notNull(authenticationSuccessHandlerList, "参数authenticationSuccessHandlerList不能为null");
        Assert.notNull(authenticationFailureHandlerList, "参数authenticationFailureHandlerList不能为null");
        this.securityConfig = securityConfig;
        this.verifyJwtTokenList = ListSortUtils.sort(verifyJwtTokenList);
        this.securityContextRepository = securityContextRepository;
        this.authenticationSuccessHandlerList = ListSortUtils.sort(authenticationSuccessHandlerList);
        this.authenticationFailureHandlerList = ListSortUtils.sort(authenticationFailureHandlerList);
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
        if (!PathFilterUtils.isAuthenticationRequest(httpRequest, securityConfig)) {
            // 不需要身份认证
            chain.doFilter(request, response);
            return;
        }
        log.debug("### 开始执行认证逻辑 ---------------------------------------------------------------------->");
        log.debug("当前请求 -> [{}]",httpRequest.getRequestURI());
        // 执行认证逻辑
        AuthenticationContext context = new AuthenticationContext(httpRequest, httpResponse);
        try {
            authentication(context);
        } catch (AuthenticationException e) {
            // 认证失败
            log.debug("### 认证失败", e);
            try {
                // 用户身份认失败处理
                AuthenticationFailureEvent event = new AuthenticationFailureEvent(context.getJwtToken(), context.getUid(), context.getClaims());
                for (AuthenticationFailureHandler handler : authenticationFailureHandlerList) {
                    handler.onAuthenticationFailure(httpRequest, httpResponse, event);
                }
                // 返回数据给客户端
                onAuthenticationFailureResponse(httpRequest, httpResponse, e);
            } catch (Exception innerException) {
                log.error("认证异常", innerException);
                // 返回数据给客户端
                HttpServletResponseUtils.sendJson(httpRequest, httpResponse, HttpStatus.INTERNAL_SERVER_ERROR, innerException);
            }
            return;
        } catch (Throwable e) {
            // 认证异常 - 返回数据给客户端
            log.error("认证异常", e);
            HttpServletResponseUtils.sendJson(httpRequest, httpResponse, HttpStatus.INTERNAL_SERVER_ERROR, e);
            return;
        } finally {
            log.debug("### 认证逻辑执行完成 <----------------------------------------------------------------------");
        }
        try {
            // 处理业务逻辑
            chain.doFilter(request, response);
        } finally {
            try {
                // 清理数据防止内存泄漏
                SecurityContextHolder.clearContext();
            } catch (Exception e) {
                log.warn("clearSecurityContext失败", e);
            }
        }
    }

    protected void authentication(AuthenticationContext context) throws Exception {
        // 用户登录身份认证
        TokenConfig tokenConfig = securityConfig.getTokenConfig();
        // 获取JWT-Token
        String jwtToken = CookieUtils.getCookie(context.getRequest(), tokenConfig.getJwtTokenName());
        if (StringUtils.isBlank(jwtToken)) {
            throw new ParserJwtTokenException("当前用户未登录");
        }
        context.setJwtToken(jwtToken);
        // 解析Token得到uid
        Claims claims = JwtTokenUtils.parserJwtToken(securityConfig.getTokenConfig(), jwtToken);
        context.setClaims(claims);
        context.setUid(claims.getSubject());
        context.getRequest().setAttribute(JWT_Object_Request_Attribute, claims);
        // 验证JWT-Token
        for (VerifyJwtToken verifyJwtToken : verifyJwtTokenList) {
            verifyJwtToken.verify(jwtToken, context.getUid(), claims, securityConfig, context.getRequest(), context.getResponse());
        }
        // 根据JWT-Token获取SecurityContext
        SecurityContext securityContext = securityContextRepository.loadContext(securityConfig.getDomainId(), context.getUid(), claims, context.getRequest(), context.getResponse());
        // 把SecurityContext绑定到当前线程和当前请求对象
        SecurityContextHolder.setContext(securityContext, context.getRequest());
        context.setSecurityContext(securityContext);
        // 用户身份认成功处理
        AuthenticationSuccessEvent event = new AuthenticationSuccessEvent(jwtToken, context.getUid(), claims, securityContext);
        for (AuthenticationSuccessHandler handler : authenticationSuccessHandlerList) {
            handler.onAuthenticationSuccess(context.getRequest(), context.getResponse(), event);
        }
    }

    /**
     * 当认证失败时响应处理
     */
    protected void onAuthenticationFailureResponse(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        if (response.isCommitted()) {
            return;
        }
        if (securityConfig.isNotLoginNeedRedirect()) {
            // 需要重定向
            HttpServletResponseUtils.redirect(response, securityConfig.getNotLoginRedirectPage());
        } else {
            // 直接返回
            HttpServletResponseUtils.sendJson(request, response, HttpStatus.UNAUTHORIZED, e);
        }
    }
}
