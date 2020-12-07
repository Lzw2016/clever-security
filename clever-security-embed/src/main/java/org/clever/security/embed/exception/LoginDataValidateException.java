package org.clever.security.embed.exception;

/**
 * 登录数据校验异常
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/12/07 20:54 <br/>
 */
public class LoginDataValidateException extends LoginException {
    public LoginDataValidateException(String msg, Throwable t) {
        super(msg, t);
    }

    public LoginDataValidateException(String msg) {
        super(msg);
    }
}
