package org.clever.security.embed.authentication.login;

import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.exception.LoginException;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 19:35 <br/>
 */
public interface VerifyLoginData extends Ordered {
    /**
     * 是否支持验证登录用户信息
     *
     * @param securityConfig 权限系统配置
     * @param request        请求对象
     * @param loginReq       登录请求参数
     * @return 返回true表示支持搜集
     */
    boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserLoginReq loginReq);

    /**
     * 用户登录验证
     *
     * @param securityConfig 权限系统配置
     * @param request        请求对象
     * @param loginReq       登录请求参数
     * @throws LoginException 验证失败
     */
    void verify(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserLoginReq loginReq) throws LoginException;
}
