package org.clever.security.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * 验证码错误
 * 作者： lzw<br/>
 * 创建时间：2018-09-19 22:33 <br/>
 */
public class BadCaptchaException extends AuthenticationException {

    public BadCaptchaException(String msg) {
        super(msg);
    }
}
