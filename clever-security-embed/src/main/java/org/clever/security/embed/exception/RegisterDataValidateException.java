package org.clever.security.embed.exception;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-10 21:13 <br/>
 */
public class RegisterDataValidateException extends RegisterException {
    public RegisterDataValidateException(String msg, Throwable t) {
        super(msg, t);
    }

    public RegisterDataValidateException(String msg) {
        super(msg);
    }
}
