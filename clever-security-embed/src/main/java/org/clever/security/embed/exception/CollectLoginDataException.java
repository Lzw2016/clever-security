package org.clever.security.embed.exception;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/05 16:08 <br/>
 */
public class CollectLoginDataException extends LoginException{
    public CollectLoginDataException(String msg, Throwable t) {
        super(msg, t);
    }

    public CollectLoginDataException(String msg) {
        super(msg);
    }
}
