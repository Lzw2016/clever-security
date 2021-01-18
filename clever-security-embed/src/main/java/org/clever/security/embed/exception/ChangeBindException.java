package org.clever.security.embed.exception;

/**
 * 更换邮箱/手机号 绑定异常
 * <p>
 * 作者：ymx <br/>
 * 创建时间：2020/11/18 20:38 <br/>
 */
public abstract class ChangeBindException extends RuntimeException {

    public ChangeBindException(String msg, Throwable t) {
        super(msg, t);
    }


    public ChangeBindException(String msg) {
        super(msg);
    }
}
