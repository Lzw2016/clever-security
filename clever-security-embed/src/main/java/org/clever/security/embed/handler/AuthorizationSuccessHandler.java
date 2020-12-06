package org.clever.security.embed.handler;

import org.clever.security.embed.event.AuthorizationSuccessEvent;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 授权成功处理
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/12/06 20:57 <br/>
 */
public interface AuthorizationSuccessHandler extends Ordered {

    /**
     * 授权成功处理
     *
     * @param request  当前请求对象
     * @param response 当前响应对象
     * @param event    授权成功事件
     */
    void onAuthorizationSuccess(HttpServletRequest request, HttpServletResponse response, AuthorizationSuccessEvent event);
}
