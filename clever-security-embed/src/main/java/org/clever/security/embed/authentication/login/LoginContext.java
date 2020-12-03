package org.clever.security.embed.authentication.login;

import lombok.Data;
import org.clever.security.embed.exception.LoginException;
import org.clever.security.model.UserInfo;
import org.clever.security.model.login.AbstractUserLoginReq;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/01 21:16 <br/>
 */
@Data
public class LoginContext {
    /**
     * 请求对象
     */
    private final HttpServletRequest request;
    /**
     * 响应对象
     */
    private final HttpServletResponse response;
    /**
     * 用户登录数据
     */
    private AbstractUserLoginReq loginData;
    /**
     * 登录异常信息
     */
    private LoginException loginException;
    /**
     * 用户信息(从数据库或其它服务加载)
     */
    private UserInfo userInfo;
    /**
     * JWT-Token
     */
    private String jwtToken;
    /**
     * 刷新Token
     */
    private String refreshToken;

    public LoginContext(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    /**
     * 是否登录失败
     */
    public boolean isLoginFailure() {
        return loginException != null;
    }
}
