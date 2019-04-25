package org.clever.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 无权登录系统
 * 作者： lzw<br/>
 * 创建时间：2018-10-01 22:01 <br/>
 */
public class CanNotLoginSysException extends AuthenticationException {

    public CanNotLoginSysException(String msg) {
        super(msg);
    }
}
