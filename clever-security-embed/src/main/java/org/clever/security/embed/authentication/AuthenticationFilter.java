package org.clever.security.embed.authentication;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.CookieUtils;
import org.clever.security.embed.authentication.token.VerifyJwtToken;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.LoginConfig;
import org.clever.security.embed.config.internal.TokenConfig;
import org.clever.security.embed.context.SecurityContextHolder;
import org.clever.security.embed.context.SecurityContextRepository;
import org.clever.security.embed.event.AuthenticationFailureEvent;
import org.clever.security.embed.event.AuthenticationSuccessEvent;
import org.clever.security.embed.exception.AuthenticationException;
import org.clever.security.embed.handler.AuthenticationFailureHandler;
import org.clever.security.embed.handler.AuthenticationSuccessHandler;
import org.clever.security.embed.utils.HttpServletResponseUtils;
import org.clever.security.embed.utils.JwtTokenUtils;
import org.clever.security.embed.utils.ListSortUtils;
import org.clever.security.model.SecurityContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
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
import java.util.Objects;

/**
 * 用户身份认证拦截器
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 21:27 <br/>
 */
@Slf4j
public class AuthenticationFilter extends GenericFilterBean {
    private static final AntPathMatcher Ant_Path_Matcher = new AntPathMatcher();
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
        if (!isAuthenticationRequest(httpRequest)) {
            // 不需要身份认证
            chain.doFilter(request, response);
            return;
        }
        log.debug("### 开始执行认证逻辑 ---------------------------------------------------------------------->");
        // 执行认证逻辑
        try {
            authentication(httpRequest, httpResponse);
        } catch (AuthenticationException e) {
            // 授权失败
            log.debug("### 认证失败", e);
            try {
                AuthenticationFailureEvent event = new AuthenticationFailureEvent();
                for (AuthenticationFailureHandler handler : authenticationFailureHandlerList) {
                    handler.onAuthenticationFailure(httpRequest, httpResponse, event);
                }
                onAuthenticationFailureResponse(httpRequest, httpResponse, e);
            } catch (Exception innerException) {
                log.error("认证异常", innerException);
                HttpServletResponseUtils.sendJson(httpRequest, httpResponse, HttpStatus.INTERNAL_SERVER_ERROR, innerException);
            }
        } catch (Throwable e) {
            // 认证异常
            log.error("认证异常", e);
            HttpServletResponseUtils.sendJson(httpRequest, httpResponse, HttpStatus.INTERNAL_SERVER_ERROR, e);
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

    protected void authentication(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 用户登录身份认证
        TokenConfig tokenConfig = securityConfig.getTokenConfig();
        // 获取JWT-Token
        String jwtToken = CookieUtils.getCookie(request, tokenConfig.getJwtTokenName());
        // 解析Token得到uid
        Claims claims = JwtTokenUtils.parserJwtToken(securityConfig.getTokenConfig(), jwtToken);
        String uid = claims.getSubject();
        // 验证JWT-Token
        for (VerifyJwtToken verifyJwtToken : verifyJwtTokenList) {
            verifyJwtToken.verify(jwtToken, uid, claims, securityConfig, request, response);
        }
        // 根据JWT-Token获取SecurityContext
        SecurityContext securityContext = securityContextRepository.loadContext(uid, claims, request, response);
        // 把SecurityContext绑定到当前线程和当前请求对象
        SecurityContextHolder.setContext(securityContext, request);
        // 用户身份认成功处理
        AuthenticationSuccessEvent event = new AuthenticationSuccessEvent();
        for (AuthenticationSuccessHandler handler : authenticationSuccessHandlerList) {
            handler.onAuthenticationSuccess(request, response, event);
        }
    }

    /**
     * 当前请求是否需要身份认证
     */
    protected boolean isAuthenticationRequest(HttpServletRequest httpRequest) {
        LoginConfig login = securityConfig.getLogin();
        List<String> ignorePaths = securityConfig.getIgnorePaths();
        // request.getRequestURI()  /a/b/c/xxx.jsp
        // request.getContextPath() /a
        // request.getServletPath() /b/c/xxx.jsp
        String path = httpRequest.getRequestURI();
        boolean postRequest = HttpMethod.POST.matches(httpRequest.getMethod());
        if (Objects.equals(login.getLoginPath(), httpRequest.getRequestURI()) && (!login.isPostOnly() || postRequest)) {
            // 当前请求是登录请求
            return false;
        }
        if (ignorePaths == null || ignorePaths.isEmpty()) {
            return false;
        }
        for (String ignorePath : ignorePaths) {
            if (Ant_Path_Matcher.match(ignorePath, path)) {
                // 忽略当前路径
                return false;
            }
        }
        return true;
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
