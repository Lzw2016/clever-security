package org.clever.security.embed.exception;

/**
 * 用户被禁用异常
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:46 <br/>
 */
public class UserDisabledException extends LoginException {
    public UserDisabledException(String msg, Throwable t) {
        super(msg, t);
    }

    public UserDisabledException(String msg) {
        super(msg);
    }
}
