package org.clever.security.embed.context;

import io.jsonwebtoken.Claims;
import org.clever.security.embed.authentication.login.LoginContext;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.model.SecurityContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 安全上下文(SecurityContext)存取器
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 22:11 <br/>
 */
public interface SecurityContextRepository {
    /**
     * 保存安全上下文(用户信息)
     *
     * @param context        用户登录上下文
     * @param securityConfig 权限系统配置
     * @param request        请求对象
     * @param response       响应对象
     */
    void saveContext(LoginContext context, SecurityConfig securityConfig, HttpServletRequest request, HttpServletResponse response);

    /**
     * 加载安全上下文(用户信息)
     *
     * @param uid      用户id
     * @param claims   JWT-Token Body内容
     * @param request  请求对象
     * @param response 响应对象
     */
    SecurityContext loadContext(String uid, Claims claims, HttpServletRequest request, HttpServletResponse response);
}
