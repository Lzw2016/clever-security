package org.clever.security.embed.handler;

import org.clever.security.embed.event.LogoutFailureEvent;
import org.springframework.core.Ordered;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:30 <br/>
 */
public interface LogoutFailureHandler extends Ordered {

    /**
     * 登出失败处理逻辑
     *
     * @param request  请求
     * @param response 响应
     * @param event    登出失败事件
     */
    void onLogoutFailure(HttpServletRequest request, HttpServletResponse response, LogoutFailureEvent event) throws IOException, ServletException;
}
