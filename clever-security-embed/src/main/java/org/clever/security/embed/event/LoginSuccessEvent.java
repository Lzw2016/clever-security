package org.clever.security.embed.event;

import lombok.Data;
import org.clever.security.embed.config.internal.LoginConfig;
import org.clever.security.model.UserInfo;
import org.clever.security.model.login.AbstractUserLoginReq;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录成功事件
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:14 <br/>
 */
@Data
public class LoginSuccessEvent {
    /**
     * 请求对象
     */
    private final HttpServletRequest request;
    /**
     * 响应对象
     */
    private final HttpServletResponse response;
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

    public LoginSuccessEvent(HttpServletRequest request, HttpServletResponse response, LoginConfig loginConfig, AbstractUserLoginReq loginData, UserInfo userInfo) {
        this.request = request;
        this.response = response;
        this.loginConfig = loginConfig;
        this.loginData = loginData;
        this.userInfo = userInfo;
    }
}
