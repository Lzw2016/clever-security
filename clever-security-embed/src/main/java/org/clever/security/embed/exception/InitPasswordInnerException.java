package org.clever.security.embed.exception;

/**
 * 登录内部异常
 * <p>
 * 作者：ymx <br/>
 * 创建时间：2021/01/18 20:33 <br/>
 */
public class InitPasswordInnerException extends UpdatePasswordException {

    public InitPasswordInnerException(String msg, Throwable t) {
        super(msg, t);
    }

    public InitPasswordInnerException(String msg) {
        super(msg);
    }
}
