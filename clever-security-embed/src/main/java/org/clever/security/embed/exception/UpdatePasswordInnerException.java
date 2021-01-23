package org.clever.security.embed.exception;

/**
 * 登录内部异常
 * <p>
 * 作者：ymx <br/>
 * 创建时间：2021/01/18 20:33 <br/>
 */
public class UpdatePasswordInnerException extends UpdatePasswordException {

    public UpdatePasswordInnerException(String msg, Throwable t) {
        super(msg, t);
    }

    public UpdatePasswordInnerException(String msg) {
        super(msg);
    }
}
