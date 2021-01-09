package org.clever.security.embed.exception;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 16:47 <br/>
 */
public class RegisterInnerException extends RegisterException{
    public RegisterInnerException(String msg, Throwable t) {
        super(msg, t);
    }

    public RegisterInnerException(String msg) {
        super(msg);
    }
}
