package org.clever.security.embed.exception;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/05 16:55 <br/>
 */
public class RepeatLoginException extends LoginException {
    public RepeatLoginException(String msg, Throwable t) {
        super(msg, t);
    }

    public RepeatLoginException(String msg) {
        super(msg);
    }
}
