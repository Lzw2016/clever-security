package org.clever.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 不支持的登录类型
 * 作者： lzw<br/>
 * 创建时间：2018-09-24 12:51 <br/>
 */
public class BadLoginTypeException extends AuthenticationException {

    public BadLoginTypeException(String msg) {
        super(msg);
    }
}
