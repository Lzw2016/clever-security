package org.clever.security.embed.authentication.logout;

import lombok.Data;
import org.clever.security.embed.exception.LogoutException;
import org.clever.security.model.SecurityContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/05 18:22 <br/>
 */
@Data
public class LogoutContext {
    /**
     * 请求对象
     */
    private final HttpServletRequest request;
    /**
     * 响应对象
     */
    private final HttpServletResponse response;
    /**
     * 登出异常信息
     */
    private LogoutException logoutException;
    /**
     * 安全上下文(用户信息)
     */
    private SecurityContext securityContext;
    /**
     * 是否登出成功
     */
    private boolean success = false;

    public LogoutContext(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
}
