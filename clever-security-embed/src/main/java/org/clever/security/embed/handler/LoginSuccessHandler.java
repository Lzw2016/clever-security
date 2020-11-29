package org.clever.security.embed.handler;

import org.clever.security.embed.event.LoginSuccessEvent;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:25 <br/>
 */
public interface LoginSuccessHandler {

    /**
     * 登录成功处理逻辑
     *
     * @param request  请求
     * @param response 响应
     * @param event    登录成功事件
     */
    void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, LoginSuccessEvent event) throws IOException, ServletException;
}
