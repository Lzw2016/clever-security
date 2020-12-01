package org.clever.security.embed.exception;

/**
 * 登录内部异常
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/12/01 21:10 <br/>
 */
public class LoginInnerException extends LoginException {
    public LoginInnerException(String msg, Throwable t) {
        super(msg, t);
    }

    public LoginInnerException(String msg) {
        super(msg);
    }
}
