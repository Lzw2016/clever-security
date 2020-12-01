package org.clever.security.embed.context;

import org.clever.security.model.SecurityContext;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 21:23 <br/>
 */
public class SecurityContextHolder {
    private static final ThreadLocal<SecurityContext> Security_Context = new ThreadLocal<>();

    public static void setContext(SecurityContext securityContext) {
        Security_Context.set(securityContext);
    }

    public static void clearContext() {
        Security_Context.remove();
    }

    public static SecurityContext getContext() {
        return Security_Context.get();
    }
}
