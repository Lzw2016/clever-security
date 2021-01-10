package org.clever.security.embed.exception;

/**
 * 作者：lizw <br/>
 * 创建时间：2020-12-16 22:23 <br/>
 */
public class AuthenticationInnerException extends AuthenticationException {
    public AuthenticationInnerException(String msg, Throwable t) {
        super(msg, t);
    }

    public AuthenticationInnerException(String msg) {
        super(msg);
    }
}
