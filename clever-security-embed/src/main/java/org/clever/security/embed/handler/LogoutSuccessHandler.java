package org.clever.security.embed.handler;

import org.clever.security.embed.event.LogoutSuccessEvent;
import org.springframework.core.Ordered;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:32 <br/>
 */
public interface LogoutSuccessHandler extends Ordered {

    /**
     * 登出成功处理逻辑
     *
     * @param request  请求
     * @param response 响应
     * @param event    登出成功事件
     */
    void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, LogoutSuccessEvent event) throws IOException, ServletException;
}
