package org.clever.security.embed.handler;

import org.clever.security.embed.event.RegisterFailureEvent;
import org.springframework.core.Ordered;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 16:09 <br/>
 */
public interface RegisterFailureHandler extends Ordered {
    /**
     * 注册失败处理逻辑
     *
     * @param request  请求
     * @param response 响应
     * @param event    注册失败事件
     */
    void onRegisterFailure(HttpServletRequest request, HttpServletResponse response, RegisterFailureEvent event) throws IOException, ServletException;
}