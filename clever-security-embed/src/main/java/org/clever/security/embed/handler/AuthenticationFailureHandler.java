package org.clever.security.embed.handler;

import org.clever.security.embed.event.AuthenticationFailureEvent;
import org.springframework.core.Ordered;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/05 18:41 <br/>
 */
public interface AuthenticationFailureHandler extends Ordered {
    /**
     * 身份认证失败处理逻辑
     *
     * @param request  请求
     * @param response 响应
     * @param event    身份认证失败事件
     */
    void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationFailureEvent event) throws IOException, ServletException;
}
