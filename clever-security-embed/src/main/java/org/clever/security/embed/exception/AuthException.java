package org.clever.security.embed.exception;

/**
 * 授权异常
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:55 <br/>
 */
public abstract class AuthException extends RuntimeException {

    public AuthException(String msg, Throwable t) {
        super(msg, t);
    }


    public AuthException(String msg) {
        super(msg);
    }
}
