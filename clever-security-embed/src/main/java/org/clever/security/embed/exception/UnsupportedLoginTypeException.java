package org.clever.security.embed.exception;

/**
 * 不支持的登录异常
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:49 <br/>
 */
public class UnsupportedLoginTypeException extends LoginException {
    public UnsupportedLoginTypeException(String msg, Throwable t) {
        super(msg, t);
    }

    public UnsupportedLoginTypeException(String msg) {
        super(msg);
    }
}
