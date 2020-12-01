package org.clever.security.embed.authentication;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.CookieUtils;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.TokenConfig;
import org.clever.security.embed.event.LogoutFailureEvent;
import org.clever.security.embed.event.LogoutSuccessEvent;
import org.clever.security.embed.handler.LogoutFailureHandler;
import org.clever.security.embed.handler.LogoutSuccessHandler;
import org.clever.security.embed.utils.ListSortUtils;
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
        Assert.notNull(securityConfig, "系统授权配置对象(SecurityConfig)不能为null");
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
        // 执行登出逻辑
        try {
            logout((HttpServletRequest) request, (HttpServletResponse) response);
        } catch (Throwable e) {
            // TODO
        }
    }

    protected void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO 获取当前登录用户信息

        // 删除JWT-Token
        Exception exception = null;
        try {
            TokenConfig tokenConfig = securityConfig.getTokenConfig();
            CookieUtils.delCookieForRooPath(request, response, tokenConfig.getJwtTokenName());
        } catch (Exception e) {
            exception = e;
        }
        // 登出成功
        if (exception == null && logoutSuccessHandlerList != null) {
            LogoutSuccessEvent logoutSuccessEvent = new LogoutSuccessEvent();
            for (LogoutSuccessHandler handler : logoutSuccessHandlerList) {
                handler.onLogoutSuccess(request, response, logoutSuccessEvent);
            }
        }
        // 登出失败
        if (exception != null && logoutFailureHandlerList != null) {
            LogoutFailureEvent logoutFailureEvent = new LogoutFailureEvent(exception);
            for (LogoutFailureHandler handler : logoutFailureHandlerList) {
                handler.onLogoutFailure(request, response, logoutFailureEvent);
            }
        }
        // 登出成功 - 返回数据给客户端
        if (exception == null) {
            // TODO 返回数据给客户端
        }
    }
}