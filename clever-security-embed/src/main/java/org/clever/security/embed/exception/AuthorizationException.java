package org.clever.security.embed.exception;

/**
 * 授权异常
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:55 <br/>
 */
public abstract class AuthorizationException extends RuntimeException {

    public AuthorizationException(String msg, Throwable t) {
        super(msg, t);
    }


    public AuthorizationException(String msg) {
        super(msg);
    }
}
