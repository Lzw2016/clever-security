package org.clever.security.embed.context;

import org.clever.security.model.SecurityContext;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 21:23 <br/>
 */
public class SecurityContextHolder {
    private static final ThreadLocal<SecurityContext> Security_Context = new ThreadLocal<>();

    // TODO 私有
    public static void setSecurityContext(SecurityContext securityContext) {
        Security_Context.set(securityContext);
    }

    // TODO 私有
    public static void removeSecurityContext() {
        Security_Context.remove();
    }

    public static SecurityContext getSecurityContext() {
        return Security_Context.get();
    }
}
