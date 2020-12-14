package org.clever.security.embed.authentication.login;

import io.jsonwebtoken.Claims;
import lombok.Data;
import org.clever.security.embed.exception.LoginException;
import org.clever.security.model.UserInfo;
import org.clever.security.model.login.AbstractUserLoginReq;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 用户登录上下文
 * <p>
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
     * JWT-Token对象
     */
    private Claims claims;
    /**
     * 刷新Token
     */
    private String refreshToken;
    /**
     * 刷新Token过期时间
     */
    private Date refreshTokenExpiredTime;

    public LoginContext(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    /**
     * 是否登录失败(无法判断登录成功)
     */
    public boolean isLoginFailure() {
        return loginException != null;
    }
}
