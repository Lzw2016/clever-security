package org.clever.security.embed.handler;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.embed.config.internal.LoginConfig;
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
public class DefaultLoginSuccessHandler implements LoginSuccessHandler {

    @Override
    public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, LoginSuccessEvent event) throws IOException, ServletException {
        // 记录登录成功日志
        addUserLoginLog();
        // 挤下最早登录的用户
        disableFirstToken(event.getLoginConfig());
    }

    protected void addUserLoginLog() {
        // TODO 记录登录成功日志user_login_log
    }

    protected void disableFirstToken(LoginConfig loginConfig) {
        if (loginConfig.getConcurrentLoginCount() <= 0) {
            return;
        }
        if (loginConfig.isAllowAfterLogin()) {
            // TODO 获取当前用户并发登录数量
            int realConcurrentLoginCount = 0;
            if (realConcurrentLoginCount > loginConfig.getConcurrentLoginCount()) {
                // TODO 挤下最早登录的用户
            }
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
