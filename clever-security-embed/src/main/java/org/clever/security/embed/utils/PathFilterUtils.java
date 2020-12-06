package org.clever.security.embed.utils;

import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.LoginConfig;
import org.clever.security.embed.config.internal.LogoutConfig;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/06 12:14 <br/>
 */
public class PathFilterUtils {
    private static final AntPathMatcher Ant_Path_Matcher = new AntPathMatcher();

    private static String getPath(HttpServletRequest request) {
        // request.getRequestURI()  /a/b/c/xxx.jsp
        // request.getContextPath() /a
        // request.getServletPath() /b/c/xxx.jsp
        return request.getRequestURI();
    }

    /**
     * 当前请求是否是登录请求
     */
    public static boolean isLoginRequest(HttpServletRequest request, SecurityConfig securityConfig) {
        final String path = getPath(request);
        LoginConfig login = securityConfig.getLogin();
        if (login == null) {
            return false;
        }
        if (!Objects.equals(login.getLoginPath(), path)) {
            return false;
        }
        boolean postRequest = HttpMethod.POST.matches(request.getMethod());
        return !login.isPostOnly() || postRequest;
    }

    /**
     * 当前请求是否是登录请求
     */
    public static boolean isLogoutRequest(HttpServletRequest request, SecurityConfig securityConfig) {
        LogoutConfig logout = securityConfig.getLogout();
        if (logout == null) {
            return false;
        }
        return Objects.equals(logout.getLogoutUrl(), getPath(request));
    }

    /**
     * 当前请求是否需要身份认证
     */
    public static boolean isAuthenticationRequest(HttpServletRequest request, SecurityConfig securityConfig) {
        // 当前请求是“登录请求”或“登出请求”
        if (isLoginRequest(request, securityConfig) || isLogoutRequest(request, securityConfig)) {
            return false;
        }
        // 不需要认证和授权的Path
        final String path = getPath(request);
        List<String> ignorePaths = securityConfig.getIgnorePaths();
        if (ignorePaths == null || ignorePaths.isEmpty()) {
            return true;
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
     * 当前请求是否需要授权
     */
    public static boolean isAuthorizationRequest(HttpServletRequest request, SecurityConfig securityConfig) {
        // 当前请求是“登录请求”或“登出请求”
        if (isLoginRequest(request, securityConfig) || isLogoutRequest(request, securityConfig)) {
            return false;
        }
        // 当前请求不需要身份认证 - 那就更不需要授权了
        if (isAuthenticationRequest(request, securityConfig)) {
            return false;
        }
        // 不需要授权的Path
        final String path = getPath(request);
        List<String> ignoreAuthPaths = securityConfig.getIgnoreAuthPaths();
        if (ignoreAuthPaths == null || ignoreAuthPaths.isEmpty()) {
            return true;
        }
        for (String ignorePath : ignoreAuthPaths) {
            if (Ant_Path_Matcher.match(ignorePath, path)) {
                // 忽略当前路径
                return false;
            }
        }
        return true;
    }
}
