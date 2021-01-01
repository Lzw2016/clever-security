package org.clever.security.embed.exception;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/27 16:39 <br/>
 */
public class SendValidateCodeException extends LoginException{
    public SendValidateCodeException(String msg, Throwable t) {
        super(msg, t);
    }

    public SendValidateCodeException(String msg) {
        super(msg);
    }
}
