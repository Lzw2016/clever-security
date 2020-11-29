package org.clever.security.embed.exception;

/**
 * 凭证(密码)错误异常
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:41 <br/>
 */
public class BadCredentialsException extends LoginException {
    public BadCredentialsException(String msg, Throwable t) {
        super(msg, t);
    }

    public BadCredentialsException(String msg) {
        super(msg);
    }
}
