package org.clever.security.embed.exception;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-16 16:29 <br/>
 */
public class PasswordRecoveryInnerException extends PasswordRecoveryException {
    public PasswordRecoveryInnerException(String message) {
        super(message);
    }

    public PasswordRecoveryInnerException(String message, Throwable cause) {
        super(message, cause);
    }
}
