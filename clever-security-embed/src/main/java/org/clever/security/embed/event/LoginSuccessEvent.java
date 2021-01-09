package org.clever.security.embed.event;

import io.jsonwebtoken.Claims;
import lombok.Data;
import org.clever.security.embed.config.internal.LoginConfig;
import org.clever.security.model.UserInfo;
import org.clever.security.model.login.AbstractUserLoginReq;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Date;

/**
 * 登录成功事件
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:14 <br/>
 */
@Data
public class LoginSuccessEvent implements Serializable {
    /**
     * 请求对象
     */
    private final HttpServletRequest request;
    /**
     * 响应对象
     */
    private final HttpServletResponse response;
    /**
     * 域id
     */
    private final Long domainId;
    /**
     * 用户登录配置
     */
    private final LoginConfig loginConfig;
    /**
     * 用户登录数据
     */
    private final AbstractUserLoginReq loginData;
    /**
     * 用户信息(从数据库或其它服务加载)
     */
    private final UserInfo userInfo;
    /**
     * JWT-Token
     */
    private final String jwtToken;
    /**
     * JWT-Token对象
     */
    private final Claims claims;
    /**
     * 刷新Token
     */
    private String refreshToken;
    /**
     * 刷新Token过期时间
     */
    private Date refreshTokenExpiredTime;

    public LoginSuccessEvent(
            HttpServletRequest request,
            HttpServletResponse response,
            Long domainId,
            LoginConfig loginConfig,
            AbstractUserLoginReq loginData,
            UserInfo userInfo,
            String jwtToken,
            Claims claims) {
        this.request = request;
        this.response = response;
        this.domainId = domainId;
        this.loginConfig = loginConfig;
        this.loginData = loginData;
        this.userInfo = userInfo;
        this.jwtToken = jwtToken;
        this.claims = claims;
    }
}
