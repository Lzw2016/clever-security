package org.clever.security.embed.event;

import lombok.Data;
import org.clever.security.model.SecurityContext;

/**
 * 登出成功事件
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:15 <br/>
 */
@Data
public class LogoutSuccessEvent {
    /**
     * 安全上下文(用户信息)
     */
    private final SecurityContext securityContext;

    public LogoutSuccessEvent(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }
}
