package org.clever.security.embed.authentication;

import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.model.UserInfo;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;

/**
 * 加载用户信息
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 19:23 <br/>
 */
public interface ILoadUser extends Ordered {
    /**
     * 是否支持加载用户信息
     *
     * @param securityConfig 系统授权配置
     * @param request        请求对象
     * @param loginReq       登录请求参数
     * @return 返回true表示支持搜集
     */
    boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserLoginReq loginReq);

    /**
     * @param securityConfig 系统授权配置
     * @param request        请求对象
     * @param loginReq       登录请求参数
     * @return 用户信息(不存在返回null)
     */
    UserInfo loadUserInfo(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserLoginReq loginReq);
}