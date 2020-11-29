package org.clever.security.embed.exception;

/**
 * 不能登录的域异常
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:50 <br/>
 */
public class CanNotLoginDomainException extends LoginException {
    public CanNotLoginDomainException(String msg, Throwable t) {
        super(msg, t);
    }

    public CanNotLoginDomainException(String msg) {
        super(msg);
    }
}
