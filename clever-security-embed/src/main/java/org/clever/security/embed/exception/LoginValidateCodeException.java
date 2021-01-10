package org.clever.security.embed.exception;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/10 21:16 <br/>
 */
public class LoginValidateCodeException extends LoginException {
    public LoginValidateCodeException(String msg, Throwable t) {
        super(msg, t);
    }

    public LoginValidateCodeException(String msg) {
        super(msg);
    }
}
