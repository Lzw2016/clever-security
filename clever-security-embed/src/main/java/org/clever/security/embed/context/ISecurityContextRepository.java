package org.clever.security.embed.context;

import org.clever.security.model.SecurityContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 22:11 <br/>
 */
public interface ISecurityContextRepository {
    SecurityContext loadContext(String uid, HttpServletRequest request, HttpServletResponse response);

    void saveContext(SecurityContext securityContext, HttpServletRequest request, HttpServletResponse response);

    boolean containsContext(HttpServletRequest request);
}
