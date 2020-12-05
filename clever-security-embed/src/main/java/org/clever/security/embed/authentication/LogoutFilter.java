package org.clever.security.embed.authentication;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.CookieUtils;
import org.clever.security.embed.authentication.logout.LogoutContext;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.LogoutConfig;
import org.clever.security.embed.config.internal.TokenConfig;
import org.clever.security.embed.context.SecurityContextHolder;
import org.clever.security.embed.event.LogoutFailureEvent;
import org.clever.security.embed.event.LogoutSuccessEvent;
import org.clever.security.embed.exception.LogoutException;
import org.clever.security.embed.exception.LogoutFailedException;
import org.clever.security.embed.exception.UnSupportLogoutException;
import org.clever.security.embed.handler.LogoutFailureHandler;
import org.clever.security.embed.handler.LogoutSuccessHandler;
import org.clever.security.embed.utils.HttpServletResponseUtils;
import org.clever.security.embed.utils.ListSortUtils;
import org.clever.security.model.SecurityContext;
import org.clever.security.model.logout.LogoutRes;
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
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:35 <br/>
 */
@Slf4j
public class LogoutFilter extends GenericFilterBean {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    /**
     * 登出成功处理
     */
    private final List<LogoutSuccessHandler> logoutSuccessHandlerList;
    /**
     * 登出失败处理
     */
    private final List<LogoutFailureHandler> logoutFailureHandlerList;

    public LogoutFilter(
            SecurityConfig securityConfig,
            List<LogoutSuccessHandler> logoutSuccessHandlerList,
            List<LogoutFailureHandler> logoutFailureHandlerList) {
        Assert.notNull(securityConfig, "权限系统配置对象(SecurityConfig)不能为null");
        this.securityConfig = securityConfig;
        this.logoutSuccessHandlerList = ListSortUtils.sort(logoutSuccessHandlerList);
        this.logoutFailureHandlerList = ListSortUtils.sort(logoutFailureHandlerList);
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
        if (!isLogoutRequest(httpRequest)) {
            // 不是登出请求
            chain.doFilter(request, response);
            return;
        }
        log.debug("### 开始执行登出逻辑 ---------------------------------------------------------------------->");
        // 执行登出逻辑
        LogoutContext context = new LogoutContext(httpRequest, httpResponse);
        try {
            logout(context);
            // 登出成功 - 返回数据给客户端
            onLogoutSuccessResponse(httpRequest, httpResponse);
        } catch (Throwable e) {
            // 登出异常
            log.error("登出异常", e);
            if (context.isSuccess()) {
                onLogoutSuccessResponse(httpRequest, httpResponse);
            } else {
                onLogoutFailureResponse(httpRequest, httpResponse, e);
            }
        } finally {
            log.debug("### 登出逻辑执行完成 <----------------------------------------------------------------------");
        }
    }

    protected void logout(LogoutContext context) throws Exception {
        context.setSecurityContext(SecurityContextHolder.getContext(context.getRequest()));
        if (context.getSecurityContext() == null) {
            context.setLogoutException(new UnSupportLogoutException("当前未登录,无法登出"));
            throw context.getLogoutException();
        }
        // 删除JWT-Token  LogoutContext
        LogoutException exception = null;
        try {
            TokenConfig tokenConfig = securityConfig.getTokenConfig();
            CookieUtils.delCookieForRooPath(context.getRequest(), context.getResponse(), tokenConfig.getJwtTokenName());
            log.debug("### 删除JWT-Token成功");
            context.setSuccess(true);
        } catch (Exception e) {
            log.debug("### 删除JWT-Token失败", e);
            exception = new LogoutFailedException("登出失败", e);
        }
        // 登出成功
        if (exception == null && logoutSuccessHandlerList != null) {
            LogoutSuccessEvent logoutSuccessEvent = new LogoutSuccessEvent();
            for (LogoutSuccessHandler handler : logoutSuccessHandlerList) {
                handler.onLogoutSuccess(context.getRequest(), context.getResponse(), logoutSuccessEvent);
            }
        }
        // 登出失败
        if (exception != null && logoutFailureHandlerList != null) {
            LogoutFailureEvent logoutFailureEvent = new LogoutFailureEvent(exception);
            for (LogoutFailureHandler handler : logoutFailureHandlerList) {
                handler.onLogoutFailure(context.getRequest(), context.getResponse(), logoutFailureEvent);
            }
        }
        if (exception != null) {
            throw exception;
        }
    }

    /**
     * 当前请求是否是登录请求
     */
    protected boolean isLogoutRequest(HttpServletRequest httpRequest) {
        LogoutConfig logout = securityConfig.getLogout();
        if (logout == null) {
            return false;
        }
        // request.getRequestURI()  /a/b/c/xxx.jsp
        // request.getContextPath() /a
        // request.getServletPath() /b/c/xxx.jsp
        return Objects.equals(logout.getLogoutUrl(), httpRequest.getRequestURI());
    }

    /**
     * 当登出成功时响应处理
     */
    protected void onLogoutSuccessResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (response.isCommitted()) {
            return;
        }
        LogoutConfig logout = securityConfig.getLogout();
        if (logout != null && logout.isLogoutNeedRedirect()) {
            // 需要重定向
            HttpServletResponseUtils.redirect(response, logout.getLogoutRedirectPage());
        } else {
            // 直接返回
            SecurityContext securityContext = SecurityContextHolder.getContext(request);
            if (securityContext == null) {
                throw new UnSupportLogoutException("当前未登录,无法登出");
            }
            LogoutRes loginRes = LogoutRes.logoutSuccess(securityContext.getUserInfo());
            HttpServletResponseUtils.sendJson(response, loginRes);
        }
    }

    /**
     * 当登出失败时响应处理
     */
    protected void onLogoutFailureResponse(HttpServletRequest request, HttpServletResponse response, Throwable e) throws IOException {
        if (response.isCommitted()) {
            return;
        }
        LogoutConfig logout = securityConfig.getLogout();
        if (logout != null && logout.isLogoutNeedRedirect()) {
            // 需要重定向
            HttpServletResponseUtils.redirect(response, logout.getLogoutRedirectPage());
        } else {
            // 直接返回
            if (e instanceof LogoutException) {
                LogoutRes loginRes = LogoutRes.logoutFailure(e.getMessage());
                HttpServletResponseUtils.sendJson(response, loginRes);
            } else {
                HttpServletResponseUtils.sendJson(request, response, HttpStatus.INTERNAL_SERVER_ERROR, e);
            }
        }
    }
}