package org.clever.security.event;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.LoginModel;
import org.clever.security.client.UserLoginLogClient;
import org.clever.security.config.SecurityConfig;
import org.clever.security.dto.request.UserLoginLogUpdateReq;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.UserLoginLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-23 19:33 <br/>
 */
@Component
@Slf4j
public class HttpSessionDestroyedListener implements ApplicationListener<SessionDestroyedEvent> {

    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private UserLoginLogClient userLoginLogClient;

    @SuppressWarnings("NullableProblems")
    @Override
    public void onApplicationEvent(SessionDestroyedEvent event) {
        // org.springframework.session.events.SessionDestroyedEvent;
        if (LoginModel.jwt.equals(securityConfig.getLoginModel())) {
            log.error("### 注销Session [{}] -> 不应该创建HttpSession", event.getId());
        } else {
            try {
                log.info("### 注销Session [{}]", event.getId());
                UserLoginLog userLoginLog = userLoginLogClient.getUserLoginLog(event.getId());
                if (userLoginLog == null) {
                    log.warn("### 注销Session未能找到对应的登录日志 [{}]", event.getId());
                    return;
                }
                // 设置登录过期
                UserLoginLogUpdateReq req = new UserLoginLogUpdateReq();
                req.setLoginState(EnumConstant.UserLoginLog_LoginState_2);
                userLoginLogClient.updateUserLoginLog(event.getId(), req);
            } catch (Exception e) {
                log.error("更新Session注销信息失败", e);
            }
        }
    }
}
