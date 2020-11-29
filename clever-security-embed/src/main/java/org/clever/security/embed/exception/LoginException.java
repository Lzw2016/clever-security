package org.clever.security.embed.exception;

/**
 * 登录异常
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:38 <br/>
 */
public abstract class LoginException extends RuntimeException {

    public LoginException(String msg, Throwable t) {
        super(msg, t);
    }


    public LoginException(String msg) {
        super(msg);
    }
}
