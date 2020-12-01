package org.clever.security.embed.handler;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.embed.event.LoginFailureEvent;
import org.springframework.core.Ordered;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/01 22:28 <br/>
 */
@Slf4j
public class BasicLoginFailureHandler implements LoginFailureHandler{
    @Override
    public void onLoginFailure(HttpServletRequest request, HttpServletResponse response, LoginFailureEvent event) throws IOException, ServletException {
        // TODO BasicLoginFailureHandler
        // 记录登录失败日志user_login_log
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
