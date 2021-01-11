package org.clever.security.embed.exception;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/11 10:00 <br/>
 */
public class RegisterValidateCodeException extends RegisterException {
    public RegisterValidateCodeException(String msg, Throwable t) {
        super(msg, t);
    }

    public RegisterValidateCodeException(String msg) {
        super(msg);
    }
}
