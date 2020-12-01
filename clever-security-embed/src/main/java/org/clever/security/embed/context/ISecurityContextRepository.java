package org.clever.security.embed.context;

import org.clever.security.model.SecurityContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 22:11 <br/>
 */
public interface ISecurityContextRepository {
    /**
     * 加载安全上下文(用户信息)
     *
     * @param uid      用户id
     * @param request  请求对象
     * @param response 响应对象
     */
    SecurityContext loadContext(String uid, HttpServletRequest request, HttpServletResponse response);

    /**
     * 保存安全上下文(用户信息)
     *
     * @param securityContext 安全上下文(用户信息)
     * @param request         请求对象
     * @param response        响应对象
     */
    void saveContext(SecurityContext securityContext, HttpServletRequest request, HttpServletResponse response);

    /**
     * 当前请求是否包含安全上下文(用户信息)
     *
     * @param request 请求对象
     */
    boolean containsContext(HttpServletRequest request);
}
