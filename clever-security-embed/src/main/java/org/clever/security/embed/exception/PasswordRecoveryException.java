package org.clever.security.embed.exception;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-16 16:28 <br/>
 */
public abstract class PasswordRecoveryException extends RuntimeException{
    public PasswordRecoveryException(String message) {
        super(message);
    }

    public PasswordRecoveryException(String message, Throwable cause) {
        super(message, cause);
    }
}
