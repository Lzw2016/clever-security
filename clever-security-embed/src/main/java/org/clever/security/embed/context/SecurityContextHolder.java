package org.clever.security.embed.context;

import org.clever.security.entity.ServerAccessToken;
import org.clever.security.model.SecurityContext;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 21:23 <br/>
 */
public class SecurityContextHolder {
    public static final String Security_Context_Attribute = SecurityContextHolder.class.getName() + "_Security_Context_Attribute";
    private static final ThreadLocal<SecurityContext> Security_Context = new ThreadLocal<>();
    private static final ThreadLocal<ServerAccessToken> ServerAccessToken_Context = new ThreadLocal<>();

    /**
     * 设置当前安全上下文(用户信息)
     *
     * @param securityContext 安全上下文(用户信息)
     * @param request         当前请求对象
     */
    public static void setContext(SecurityContext securityContext, HttpServletRequest request) {
        Assert.notNull(securityContext, "参数securityContext不能为null");
        Assert.notNull(request, "参数request不能为null");
        request.setAttribute(Security_Context_Attribute, securityContext);
        Security_Context.set(securityContext);
    }

    /**
     * 清除当前安全上下文(用户信息)
     */
    public static void clearContext() {
        Security_Context.remove();
    }


    /**
     * 当前请求是否包含安全上下文(用户信息)
     *
     * @param request 请求对象
     */
    public static boolean containsContext(HttpServletRequest request) {
        Assert.notNull(request, "参数request不能为null");
        Object securityContext = request.getAttribute(Security_Context_Attribute);
        return (securityContext instanceof SecurityContext);
    }

    /**
     * 获取当前安全上下文(用户信息)
     */
    public static SecurityContext getContext() {
        return Security_Context.get();
    }

    /**
     * 获取当前安全上下文(用户信息)
     *
     * @param request 请求对象
     */
    public static SecurityContext getContext(HttpServletRequest request) {
        Assert.notNull(request, "参数request不能为null");
        Object securityContext = request.getAttribute(Security_Context_Attribute);
        if (securityContext instanceof SecurityContext) {
            return (SecurityContext) securityContext;
        } else {
            return null;
        }
    }

    /**
     * 当前是用户访问
     */
    public static boolean isUserAccess() {
        return Security_Context.get() != null;
    }

    /**
     * 当前是服务直接的访问
     */
    public static boolean isServerAccess() {
        return ServerAccessToken_Context.get() != null;
    }

    /**
     * 设置ServerAccessToken
     */
    public static void setServerAccessToken(ServerAccessToken token) {
        Assert.notNull(token, "参数token不能为null");
        ServerAccessToken_Context.set(token);
    }

    /**
     * 获取ServerAccessToken
     */
    public static ServerAccessToken getServerAccessToken() {
        return ServerAccessToken_Context.get();
    }

    /**
     * 清除ServerAccessToken
     */
    public static void clearServerAccessToken() {
        ServerAccessToken_Context.remove();
    }
}
