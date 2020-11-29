package org.clever.security.embed.exception;

/**
 * 并发登录异常(单用户超过了最大并发登录数)
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:51 <br/>
 */
public class ConcurrentLoginException extends LoginException {
    public ConcurrentLoginException(String msg, Throwable t) {
        super(msg, t);
    }

    public ConcurrentLoginException(String msg) {
        super(msg);
    }
}
