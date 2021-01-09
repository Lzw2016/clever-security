package org.clever.security.embed.handler;

import org.clever.security.embed.event.RegisterSuccessEvent;
import org.springframework.core.Ordered;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 16:09 <br/>
 */
public interface RegisterSuccessHandler extends Ordered {
    /**
     * 注册成功处理逻辑
     *
     * @param request  请求
     * @param response 响应
     * @param event    注册成功事件
     */
    void onRegisterSuccess(HttpServletRequest request, HttpServletResponse response, RegisterSuccessEvent event) throws IOException, ServletException;
}
