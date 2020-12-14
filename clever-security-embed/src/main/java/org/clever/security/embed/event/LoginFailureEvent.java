package org.clever.security.embed.event;

import lombok.Data;
import org.clever.security.embed.exception.LoginException;
import org.clever.security.model.UserInfo;
import org.clever.security.model.login.AbstractUserLoginReq;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录失败事件
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:13 <br/>
 */
@Data
public class LoginFailureEvent {
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
     * 用户登录数据
     */
    private final AbstractUserLoginReq loginData;
    /**
     * 用户信息(从数据库或其它服务加载)
     */
    private final UserInfo userInfo;
    /**
     * 登录异常对象
     */
    private final LoginException loginException;

    public LoginFailureEvent(HttpServletRequest request, HttpServletResponse response, Long domainId, AbstractUserLoginReq loginData, UserInfo userInfo, LoginException loginException) {
        this.request = request;
        this.response = response;
        this.domainId = domainId;
        this.loginData = loginData;
        this.userInfo = userInfo;
        this.loginException = loginException;
    }
}
