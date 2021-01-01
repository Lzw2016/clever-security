package org.clever.security.embed.exception;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/26 14:13 <br/>
 */
public class InvalidJwtRefreshTokenException extends AuthenticationException{
    public InvalidJwtRefreshTokenException(String msg, Throwable t) {
        super(msg, t);
    }

    public InvalidJwtRefreshTokenException(String msg) {
        super(msg);
    }
}
