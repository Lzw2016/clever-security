package org.clever.security.embed.exception;

/**
 * 注册异常
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 15:57 <br/>
 */
public abstract class RegisterException extends RuntimeException {

    public RegisterException(String msg, Throwable t) {
        super(msg, t);
    }

    public RegisterException(String msg) {
        super(msg);
    }
}
