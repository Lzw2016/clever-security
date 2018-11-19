package org.clever.security.jwt.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-19 20:41 <br/>
 */
public class ConcurrentLoginException extends AuthenticationException {
    public ConcurrentLoginException(String msg) {
        super(msg);
    }
}
