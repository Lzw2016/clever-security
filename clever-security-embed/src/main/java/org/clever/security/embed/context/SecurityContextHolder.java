package org.clever.security.embed.context;

import org.clever.security.model.SecurityContext;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 21:23 <br/>
 */
public class SecurityContextHolder {
    private static final ThreadLocal<SecurityContext> Security_Context = new ThreadLocal<>();

    /**
     * 设置当前安全上下文(用户信息)
     */
    public static void setContext(SecurityContext securityContext) {
        Security_Context.set(securityContext);
    }

    /**
     * 清除当前安全上下文(用户信息)
     */
    public static void clearContext() {
        Security_Context.remove();
    }

    /**
     * 获取当前安全上下文(用户信息)
     */
    public static SecurityContext getContext() {
        return Security_Context.get();
    }
}
