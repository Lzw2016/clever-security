package org.clever.security.embed.exception;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/05 17:29 <br/>
 */
public class LogoutFailedException extends LogoutException{
    public LogoutFailedException(String msg, Throwable t) {
        super(msg, t);
    }

    public LogoutFailedException(String msg) {
        super(msg);
    }
}
