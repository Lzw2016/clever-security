package org.clever.security.embed.exception;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 16:46 <br/>
 */
public class CollectRegisterDataException extends RegisterException {
    public CollectRegisterDataException(String msg, Throwable t) {
        super(msg, t);
    }

    public CollectRegisterDataException(String msg) {
        super(msg);
    }
}
