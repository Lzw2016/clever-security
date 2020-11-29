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
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:35 <br/>
 */
@Slf4j
public class LogoutInterceptor implements HandlerInterceptor {
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

    public LogoutInterceptor(
            SecurityConfig securityConfig,
            List<LogoutSuccessHandler> logoutSuccessHandlerList,
            List<LogoutFailureHandler> logoutFailureHandlerList) {
        Assert.notNull(securityConfig, "系统授权配置对象(SecurityConfig)不能为null");
        this.securityConfig = securityConfig;
        this.logoutSuccessHandlerList = ListSortUtils.sort(logoutSuccessHandlerList);
        this.logoutFailureHandlerList = ListSortUtils.sort(logoutFailureHandlerList);
    }

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nullable Object handler) {
        try {
            logout(request, response);
        } catch (Exception e) {
            // TODO
        }
        return false;
    }

    protected void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO 获取当前登录用户信息

        // 删除JWT-Token
        Throwable throwable = null;
        try {
            TokenConfig tokenConfig = securityConfig.getTokenConfig();
            CookieUtils.delCookieForRooPath(request, response, tokenConfig.getJwtTokenName());
        } catch (Exception e) {
            throwable = e;
        }
        // 登出成功
        if (throwable == null && logoutSuccessHandlerList != null) {
            LogoutSuccessEvent logoutSuccessEvent = new LogoutSuccessEvent();
            for (LogoutSuccessHandler handler : logoutSuccessHandlerList) {
                handler.onLogoutSuccess(request, response, logoutSuccessEvent);
            }
        }
        // 登出失败
        if (throwable != null && logoutFailureHandlerList != null) {
            LogoutFailureEvent logoutFailureEvent = new LogoutFailureEvent(throwable);
            for (LogoutFailureHandler handler : logoutFailureHandlerList) {
                handler.onLogoutFailure(request, response, logoutFailureEvent);
            }
        }
    }
}