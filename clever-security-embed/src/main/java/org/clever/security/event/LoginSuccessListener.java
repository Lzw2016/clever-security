package org.clever.security.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * 登录事件监听
 * 作者： lzw<br/>
 * 创建时间：2018-09-18 13:55 <br/>
 */
@Component
@Slf4j
public class LoginSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        log.info("### 登录成功 -> {}", authentication);
    }
}
