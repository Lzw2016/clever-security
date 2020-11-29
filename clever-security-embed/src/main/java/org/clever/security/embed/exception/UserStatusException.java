package org.clever.security.embed.exception;

/**
 * 用户状态异常
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:44 <br/>
 */
public class UserStatusException extends LoginException {
    public UserStatusException(String msg, Throwable t) {
        super(msg, t);
    }

    public UserStatusException(String msg) {
        super(msg);
    }
}
