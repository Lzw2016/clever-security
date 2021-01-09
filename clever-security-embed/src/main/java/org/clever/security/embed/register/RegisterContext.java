package org.clever.security.embed.register;

import lombok.Data;
import org.clever.security.embed.exception.RegisterException;
import org.clever.security.entity.User;
import org.clever.security.model.register.AbstractUserRegisterReq;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 15:55 <br/>
 */
@Data
public class RegisterContext {
    /**
     * 请求对象
     */
    private final HttpServletRequest request;
    /**
     * 响应对象
     */
    private final HttpServletResponse response;
    /**
     * 注册数据对象
     */
    private AbstractUserRegisterReq registerData;
    /**
     * 注册异常信息
     */
    private RegisterException registerException;
    /**
     * 注册的用户信息
     */
    private User user;

    public RegisterContext(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
}
