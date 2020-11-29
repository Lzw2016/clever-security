package org.clever.security.embed.handler;

import org.clever.security.embed.event.LoginFailureEvent;
import org.springframework.core.Ordered;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:22 <br/>
 */
public interface LoginFailureHandler extends Ordered {
    /**
     * 登录失败处理逻辑
     *
     * @param request  请求
     * @param response 响应
     * @param event    登录失败事件
     */
    void onLoginFailure(HttpServletRequest request, HttpServletResponse response, LoginFailureEvent event) throws IOException, ServletException;
}
