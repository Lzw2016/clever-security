package org.clever.security.embed.login;

import org.clever.security.embed.config.SecurityConfig;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户身份认证拦截器(登录拦截器)
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:09 <br/>
 */
public class LoginInterceptor implements HandlerInterceptor {

    private final SecurityConfig securityConfig;

    public LoginInterceptor(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
