package org.clever.security.embed.exception;

/**
 * 用户认证异常
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/12/02 22:41 <br/>
 */
public abstract class AuthenticationException extends RuntimeException {
    public AuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }


    public AuthenticationException(String msg) {
        super(msg);
    }
}