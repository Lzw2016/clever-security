package org.clever.security.embed.exception;

/**
 * 解析JWT-Token异常
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/12/02 22:42 <br/>
 */
public class ParserJwtTokenException extends AuthenticationException {
    public ParserJwtTokenException(String msg, Throwable t) {
        super(msg, t);
    }

    public ParserJwtTokenException(String msg) {
        super(msg);
    }
}
