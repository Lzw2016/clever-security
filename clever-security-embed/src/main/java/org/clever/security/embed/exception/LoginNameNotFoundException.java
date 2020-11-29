package org.clever.security.embed.exception;

/**
 * 登录名不存在异常
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:40 <br/>
 */
public class LoginNameNotFoundException extends LoginException {
    public LoginNameNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public LoginNameNotFoundException(String msg) {
        super(msg);
    }
}
