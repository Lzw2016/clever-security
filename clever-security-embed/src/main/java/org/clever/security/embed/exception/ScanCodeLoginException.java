package org.clever.security.embed.exception;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/10 20:58 <br/>
 */
public class ScanCodeLoginException extends LoginException {
    public ScanCodeLoginException(String msg, Throwable t) {
        super(msg, t);
    }

    public ScanCodeLoginException(String msg) {
        super(msg);
    }
}
