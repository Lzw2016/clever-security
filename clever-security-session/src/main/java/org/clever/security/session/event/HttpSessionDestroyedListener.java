package org.clever.security.session.event;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.UserLoginLogClient;
import org.clever.security.dto.request.UserLoginLogUpdateReq;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.UserLoginLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.session.Session;
import org.springframework.session.events.SessionDestroyedEvent;
import org.springframework.stereotype.Component;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-23 19:33 <br/>
 */
@SuppressWarnings("NullableProblems")
@Component
@Slf4j
public class HttpSessionDestroyedListener implements ApplicationListener<SessionDestroyedEvent> {

    @Autowired
    private UserLoginLogClient userLoginLogClient;

    @Override
    public void onApplicationEvent(SessionDestroyedEvent event) {
        try {
            Session session = event.getSession();
            log.info("### 注销Session [{}]", session.getId());
            UserLoginLog userLoginLog = userLoginLogClient.getUserLoginLog(session.getId());
            if (userLoginLog == null) {
                log.warn("### 注销Session未能找到对应的登录日志 [{}]", session.getId());
                return;
            }
            // 设置登录过期
            UserLoginLogUpdateReq req = new UserLoginLogUpdateReq();
            req.setLoginState(EnumConstant.UserLoginLog_LoginState_2);
            userLoginLogClient.updateUserLoginLog(session.getId(), req);
        } catch (Exception e) {
            log.error("更新Session注销信息失败", e);
        }
    }
}
