package org.clever.security.embed.exception;

/**
 * 登出异常
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:39 <br/>
 */
public abstract class LogoutException extends RuntimeException {

    public LogoutException(String msg, Throwable t) {
        super(msg, t);
    }

    public LogoutException(String msg) {
        super(msg);
    }
}
