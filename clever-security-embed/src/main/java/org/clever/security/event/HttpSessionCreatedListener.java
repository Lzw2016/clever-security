package org.clever.security.event;//package org.clever.security.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * Session 创建监听
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-09-23 19:28 <br/>
 */
@Component
@Slf4j
public class HttpSessionCreatedListener implements ApplicationListener<HttpSessionCreatedEvent> {

    @Override
    public void onApplicationEvent(HttpSessionCreatedEvent event) {
        HttpSession session = event.getSession();
        log.error("### 新建Session [{}] -> JWT Token 不应该创建HttpSession", session.getId());
    }
}
