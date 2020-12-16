package org.clever.security.embed.exception;

/**
 * 作者：lizw <br/>
 * 创建时间：2020-12-16 21:05 <br/>
 */
public class InvalidJwtTokenException extends AuthenticationException {
    public InvalidJwtTokenException(String msg, Throwable t) {
        super(msg, t);
    }

    public InvalidJwtTokenException(String msg) {
        super(msg);
    }
}
