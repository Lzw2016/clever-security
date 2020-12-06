package org.clever.security.embed.authorization;

import lombok.Data;
import org.clever.security.embed.exception.AuthorizationException;
import org.clever.security.model.SecurityContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/06 12:33 <br/>
 */
@Data
public class AuthorizationContext {
    /**
     * 请求对象
     */
    private final HttpServletRequest request;
    /**
     * 响应对象
     */
    private final HttpServletResponse response;
    /**
     * 安全上下文(用户信息)
     */
    private SecurityContext securityContext;
    /**
     * 授权异常信息
     */
    private AuthorizationException authorizationException;

    public AuthorizationContext(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
}
