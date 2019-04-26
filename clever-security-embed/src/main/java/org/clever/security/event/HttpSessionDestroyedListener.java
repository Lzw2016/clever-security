package org.clever.security.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-23 19:33 <br/>
 */
@SuppressWarnings("NullableProblems")
@Component
@Slf4j
public class HttpSessionDestroyedListener implements ApplicationListener<SessionDestroyedEvent> {

    @Override
    public void onApplicationEvent(SessionDestroyedEvent event) {
        log.error("### 注销Session [{}] -> 不应该创建HttpSession", event.getId());
    }
}
