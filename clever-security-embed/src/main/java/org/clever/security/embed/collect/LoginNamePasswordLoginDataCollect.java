package org.clever.security.embed.collect;

import com.alibaba.fastjson.JSONObject;
import org.clever.security.LoginChannel;
import org.clever.security.LoginType;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.utils.HttpServletRequestUtils;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.clever.security.model.login.LoginNamePasswordReq;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:05 <br/>
 */
public class LoginNamePasswordLoginDataCollect implements LoginDataCollect {

    @Override
    public boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request) {
        return LoginType.LoginName_Password.equals(LoginType.valueOf(request.getParameter("LoginType")));
    }

    @Override
    public AbstractUserLoginReq collectLoginData(SecurityConfig securityConfig, HttpServletRequest request) {
        // TODO collectLoginData
        JSONObject bodyData = HttpServletRequestUtils.getBodyData(request);
        if (bodyData != null) {
            LoginNamePasswordReq loginNamePasswordReq = new LoginNamePasswordReq();
            loginNamePasswordReq.setLoginName(bodyData.getString("loginName"));
            loginNamePasswordReq.setPassword(bodyData.getString("password"));
            loginNamePasswordReq.setLoginChannel(LoginChannel.valueOf(bodyData.getString("LoginChannel")));
            loginNamePasswordReq.setRememberMe(Boolean.parseBoolean(bodyData.getString("rememberMe")));
            return loginNamePasswordReq;
        }
        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
