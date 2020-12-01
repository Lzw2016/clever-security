package org.clever.security.embed.handler;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.embed.event.LoginSuccessEvent;
import org.springframework.core.Ordered;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/01 22:31 <br/>
 */
@Slf4j
public class BasicLoginSuccessHandler implements LoginSuccessHandler {
    @Override
    public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, LoginSuccessEvent event) throws IOException, ServletException {
        // TODO BasicLoginSuccessHandler
        // 记录登录成功日志user_login_log
        // 加载安全上下文(用户信息)
        // 保存安全上下文(用户信息)
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
