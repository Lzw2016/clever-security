package org.clever.security.embed.exception;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-16 16:29 <br/>
 */
public class UpdatePasswordValidateCodeException extends UpdatePasswordException {

    public UpdatePasswordValidateCodeException(String message) {
        super(message);
    }

    public UpdatePasswordValidateCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
