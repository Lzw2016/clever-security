package org.clever.security.embed.exception;

/**
 * 设置/修改密码 异常
 * <p>
 * 作者：ymx <br/>
 * 创建时间：2020/11/18 20:38 <br/>
 */
public abstract class UpdatePasswordException extends RuntimeException {

    public UpdatePasswordException(String msg, Throwable t) {
        super(msg, t);
    }


    public UpdatePasswordException(String msg) {
        super(msg);
    }
}
