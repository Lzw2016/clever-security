package org.clever.security.embed.exception;

/**
 * 授权内部异常
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/12/06 12:35 <br/>
 */
public class AuthorizationInnerException extends AuthorizationException {
    public AuthorizationInnerException(String msg, Throwable t) {
        super(msg, t);
    }

    public AuthorizationInnerException(String msg) {
        super(msg);
    }
}
