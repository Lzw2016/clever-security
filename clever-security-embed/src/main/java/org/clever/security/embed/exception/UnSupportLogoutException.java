package org.clever.security.embed.exception;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/05 17:26 <br/>
 */
public class UnSupportLogoutException extends LogoutException {
    public UnSupportLogoutException(String msg, Throwable t) {
        super(msg, t);
    }

    public UnSupportLogoutException(String msg) {
        super(msg);
    }
}
