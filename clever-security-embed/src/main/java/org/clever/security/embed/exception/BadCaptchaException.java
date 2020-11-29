package org.clever.security.embed.exception;

/**
 * 验证码错误异常
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:48 <br/>
 */
public class BadCaptchaException extends LoginException {
    public BadCaptchaException(String msg, Throwable t) {
        super(msg, t);
    }

    public BadCaptchaException(String msg) {
        super(msg);
    }
}
