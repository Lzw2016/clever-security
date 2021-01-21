package org.clever.security.embed.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.utils.CookieUtils;
import org.clever.common.utils.DateTimeUtils;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.dto.request.UseJwtRefreshTokenReq;
import org.clever.security.embed.authentication.token.AuthenticationContext;
import org.clever.security.embed.authentication.token.VerifyJwtToken;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.TokenConfig;
import org.clever.security.embed.context.SecurityContextHolder;
import org.clever.security.embed.context.SecurityContextRepository;
import org.clever.security.embed.event.AuthenticationFailureEvent;
import org.clever.security.embed.event.AuthenticationSuccessEvent;
import org.clever.security.embed.exception.AuthenticationException;
import org.clever.security.embed.exception.InvalidJwtRefreshTokenException;
import org.clever.security.embed.exception.ParserJwtTokenException;
import org.clever.security.embed.handler.AuthenticationFailureHandler;
import org.clever.security.embed.handler.AuthenticationSuccessHandler;
import org.clever.security.embed.utils.HttpServletResponseUtils;
import org.clever.security.embed.utils.JwtTokenUtils;
import org.clever.security.embed.utils.ListSortUtils;
import org.clever.security.embed.utils.PathFilterUtils;
import org.clever.security.entity.JwtToken;
import org.clever.security.model.SecurityContext;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 用户身份认证拦截器
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 21:27 <br/>
 */
@Slf4j
public class AuthenticationFilter extends HttpFilter {
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
    /**
     * 登录支持对象
     */
    private final LoginSupportClient loginSupportClient;

    public AuthenticationFilter(
            SecurityConfig securityConfig,
            List<VerifyJwtToken> verifyJwtTokenList,
            SecurityContextRepository securityContextRepository,
            List<AuthenticationSuccessHandler> authenticationSuccessHandlerList,
            List<AuthenticationFailureHandler> authenticationFailureHandlerList,
            LoginSupportClient loginSupportClient) {
        Assert.notNull(securityConfig, "权限系统配置对象(SecurityConfig)不能为null");
        Assert.notEmpty(verifyJwtTokenList, "JWT-Token验证器(VerifyJwtToken)不存在");
        Assert.notNull(securityContextRepository, "安全上下文存取器(SecurityContextRepository)不能为null");
        Assert.notNull(authenticationSuccessHandlerList, "参数authenticationSuccessHandlerList不能为null");
        Assert.notNull(authenticationFailureHandlerList, "参数authenticationFailureHandlerList不能为null");
        Assert.notNull(loginSupportClient, "参数loginSupportClient不能为null");
        this.securityConfig = securityConfig;
        this.verifyJwtTokenList = ListSortUtils.sort(verifyJwtTokenList);
        this.securityContextRepository = securityContextRepository;
        this.authenticationSuccessHandlerList = ListSortUtils.sort(authenticationSuccessHandlerList);
        this.authenticationFailureHandlerList = ListSortUtils.sort(authenticationFailureHandlerList);
        this.loginSupportClient = loginSupportClient;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!PathFilterUtils.isAuthenticationRequest(request, securityConfig)) {
            // 不需要身份认证
            if (!securityConfig.getLogin().isAllowRepeatLogin() && PathFilterUtils.isLoginRequest(request, securityConfig)) {
                // 当前请求是登录请求且不允许重复登录时，需要判断当前用户是否已经登录
                doAuthentication(request, response, false);
            }
            innerDoFilter(request, response, chain);
            return;
        }
        if (doAuthentication(request, response, true)) {
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
                SecurityContextHolder.clearContext();
            } catch (Exception e) {
                log.warn("clearSecurityContext失败", e);
            }
        }
    }

    /**
     * 执行用户身份认证
     *
     * @param request      请求对象
     * @param response     响应对象
     * @param sendResponse 是否需要返回数据给客户端
     * @return true:认证成功, false:认证失败
     */
    protected boolean doAuthentication(HttpServletRequest request, HttpServletResponse response, boolean sendResponse) throws IOException {
        log.debug("### 开始执行认证逻辑 ---------------------------------------------------------------------->");
        log.debug("当前请求 -> [{}]", request.getRequestURI());
        // 执行认证逻辑
        AuthenticationContext context = new AuthenticationContext(request, response);
        try {
            authentication(context);
            // 用户身份认成功处理
            authenticationSuccessHandler(context);
        } catch (AuthenticationException e) {
            // 认证失败
            log.debug("### 认证失败", e);
            try {
                // 用户身份认失败处理
                authenticationFailureHandler(context);
                // 返回数据给客户端
                if (sendResponse) {
                    onAuthenticationFailureResponse(request, response, e);
                }
            } catch (Exception innerException) {
                log.error("认证异常", innerException);
                // 返回数据给客户端
                if (sendResponse) {
                    HttpServletResponseUtils.sendJson(request, response, HttpStatus.INTERNAL_SERVER_ERROR, innerException);
                }
            }
            return false;
        } catch (Throwable e) {
            // 认证异常 - 返回数据给客户端
            log.error("认证异常", e);
            if (sendResponse) {
                HttpServletResponseUtils.sendJson(request, response, HttpStatus.INTERNAL_SERVER_ERROR, e);
            }
            return false;
        } finally {
            log.debug("### 认证逻辑执行完成 <----------------------------------------------------------------------");
        }
        return true;
    }

    /**
     * 认证流程
     */
    protected void authentication(AuthenticationContext context) throws Exception {
        // 用户登录身份认证
        TokenConfig tokenConfig = securityConfig.getTokenConfig();
        // 获取JWT-Token
        String jwtToken;
        String refreshToken;
        if (tokenConfig.isUseCookie()) {
            jwtToken = CookieUtils.getCookie(context.getRequest(), tokenConfig.getJwtTokenName());
            refreshToken = CookieUtils.getCookie(context.getRequest(), tokenConfig.getRefreshTokenName());
        } else {
            jwtToken = context.getRequest().getHeader(tokenConfig.getJwtTokenName());
            refreshToken = context.getRequest().getHeader(tokenConfig.getRefreshTokenName());
        }
        if (StringUtils.isBlank(jwtToken)) {
            throw new ParserJwtTokenException("当前用户未登录");
        }
        // 解析Token得到uid
        Claims claims;
        try {
            claims = JwtTokenUtils.parserJwtToken(securityConfig.getTokenConfig(), jwtToken);
        } catch (ParserJwtTokenException e) {
            if (!tokenConfig.isEnableRefreshToken() || !(e.getCause() instanceof ExpiredJwtException)) {
                throw e;
            }
            // 验证刷新Token - 重新生成JWT-Token
            log.debug("开始验证刷新Token | refresh-token={}", refreshToken);
            if (StringUtils.isBlank(refreshToken)) {
                throw new InvalidJwtRefreshTokenException("刷新Token为空", e);
            }
            // 使用刷新Token创建新的JWT-Token
            UseJwtRefreshTokenReq req = new UseJwtRefreshTokenReq(securityConfig.getDomainId());
            claims = JwtTokenUtils.readClaims(jwtToken);
            req.setUseJwtId(Long.parseLong(claims.getId()));
            req.setUseRefreshToken(refreshToken);
            // 创建新的JWT-Token
            final Date now = new Date();
            jwtToken = JwtTokenUtils.createJwtToken(tokenConfig, claims);
            refreshToken = JwtTokenUtils.createRefreshToken(claims.getSubject());
            req.setJwtId(Long.parseLong(claims.getId()));
            req.setToken(jwtToken);
            req.setExpiredTime(claims.getExpiration());
            req.setRefreshToken(refreshToken);
            req.setRefreshTokenExpiredTime(new Date(now.getTime() + tokenConfig.getRefreshTokenValidity().toMillis()));
            // 使用刷新Token
            JwtToken newJwtToken = loginSupportClient.useJwtRefreshToken(req);
            if (newJwtToken == null) {
                throw new InvalidJwtRefreshTokenException("无效的刷新Token");
            }
            log.debug("刷新Token验证成功 | uid={} | jwt-token={} | refresh-token={}", newJwtToken.getUid(), newJwtToken.getToken(), newJwtToken.getRefreshToken());
            // 更新客户端Token数据
            if (tokenConfig.isUseCookie()) {
                int maxAge = DateTimeUtils.pastSeconds(now, newJwtToken.getExpiredTime()) + (60 * 3);
                CookieUtils.setCookie(context.getResponse(), "/", tokenConfig.getJwtTokenName(), newJwtToken.getToken(), maxAge);
                int refreshTokenMaxAge = DateTimeUtils.pastSeconds(now, newJwtToken.getRefreshTokenExpiredTime()) + (60 * 3);
                CookieUtils.setCookie(context.getResponse(), "/", tokenConfig.getRefreshTokenName(), newJwtToken.getRefreshToken(), Integer.max(refreshTokenMaxAge, maxAge));
            } else {
                context.getResponse().addHeader(tokenConfig.getJwtTokenName(), newJwtToken.getToken());
                context.getResponse().addHeader(tokenConfig.getRefreshTokenName(), newJwtToken.getRefreshToken());
            }
        }
        context.setJwtToken(jwtToken);
        context.setRefreshToken(refreshToken);
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
    }

    /**
     * 用户身份认成功处理
     */
    protected void authenticationSuccessHandler(AuthenticationContext context) throws Exception {
        if (authenticationSuccessHandlerList == null) {
            return;
        }
        AuthenticationSuccessEvent event = new AuthenticationSuccessEvent(
                context.getJwtToken(),
                context.getUid(),
                context.getClaims(),
                context.getSecurityContext()
        );
        for (AuthenticationSuccessHandler handler : authenticationSuccessHandlerList) {
            handler.onAuthenticationSuccess(context.getRequest(), context.getResponse(), event);
        }
    }

    /**
     * 用户身份认失败处理
     */
    protected void authenticationFailureHandler(AuthenticationContext context) throws Exception {
        if (authenticationFailureHandlerList == null) {
            return;
        }
        AuthenticationFailureEvent event = new AuthenticationFailureEvent(context.getJwtToken(), context.getUid(), context.getClaims());
        for (AuthenticationFailureHandler handler : authenticationFailureHandlerList) {
            handler.onAuthenticationFailure(context.getRequest(), context.getResponse(), event);
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
