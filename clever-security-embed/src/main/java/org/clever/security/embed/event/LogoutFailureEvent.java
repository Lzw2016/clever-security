package org.clever.security.embed.event;

import lombok.Data;

/**
 * 登出失败事件
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:15 <br/>
 */
@Data
public class LogoutFailureEvent {
    private final Throwable throwable;

    public LogoutFailureEvent(Throwable throwable) {
        this.throwable = throwable;
    }
}
