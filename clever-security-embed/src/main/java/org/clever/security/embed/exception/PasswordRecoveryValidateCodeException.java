package org.clever.security.embed.exception;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-16 16:29 <br/>
 */
public class PasswordRecoveryValidateCodeException extends PasswordRecoveryException {
    public PasswordRecoveryValidateCodeException(String message) {
        super(message);
    }

    public PasswordRecoveryValidateCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
