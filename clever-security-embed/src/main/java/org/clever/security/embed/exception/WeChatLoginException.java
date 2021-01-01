package org.clever.security.embed.exception;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/27 15:50 <br/>
 */
public class WeChatLoginException extends LoginException {
    public WeChatLoginException(String msg, Throwable t) {
        super(msg, t);
    }

    public WeChatLoginException(String msg) {
        super(msg);
    }
}
