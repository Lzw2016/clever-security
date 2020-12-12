package org.clever.security.embed.handler;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.SnowFlake;
import org.clever.security.embed.event.LoginFailureEvent;
import org.clever.security.entity.UserLoginLog;
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
public class DefaultLoginFailureHandler implements LoginFailureHandler {
    @Override
    public void onLoginFailure(HttpServletRequest request, HttpServletResponse response, LoginFailureEvent event) throws IOException, ServletException {
        // 记录登录失败日志
        addUserLoginLog();
    }

    protected void addUserLoginLog() {
        // TODO 记录登录失败日志user_login_log
        UserLoginLog userLoginLog = new UserLoginLog();
        userLoginLog.setId(SnowFlake.SNOW_FLAKE.nextId());
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
