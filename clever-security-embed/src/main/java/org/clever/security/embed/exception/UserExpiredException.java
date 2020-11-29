package org.clever.security.embed.exception;

/**
 * 用户过期异常
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:45 <br/>
 */
public class UserExpiredException extends LoginException {
    public UserExpiredException(String msg, Throwable t) {
        super(msg, t);
    }

    public UserExpiredException(String msg) {
        super(msg);
    }
}
