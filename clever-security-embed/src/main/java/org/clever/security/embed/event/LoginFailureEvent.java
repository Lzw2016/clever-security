package org.clever.security.embed.event;

import lombok.Data;
import org.clever.security.embed.exception.LoginException;

/**
 * 登录失败事件
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:13 <br/>
 */
@Data
public class LoginFailureEvent {
    /**
     * 登录异常对象
     */
    private final LoginException loginException;

    public LoginFailureEvent(LoginException loginException) {
        this.loginException = loginException;
    }
}
