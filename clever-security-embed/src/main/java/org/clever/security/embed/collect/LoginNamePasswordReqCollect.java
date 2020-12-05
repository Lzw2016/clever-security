package org.clever.security.embed.collect;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.clever.security.LoginChannel;
import org.clever.security.LoginType;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.LoginConfig;
import org.clever.security.embed.utils.HttpServletRequestUtils;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.clever.security.model.login.LoginNamePasswordReq;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 收集“用户名/密码”登录数据
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:05 <br/>
 */
public class LoginNamePasswordReqCollect implements LoginDataCollect {
    @Override
    public boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request) {
        String loginType = request.getParameter(AbstractUserLoginReq.LoginType_ParamName);
        LoginType loginTypeEnum = LoginType.lookup(loginType);
        if (loginTypeEnum == null) {
            LoginNamePasswordReq req = getLoginNamePasswordReq(securityConfig, request);
            return req != null;
        }
        return Objects.equals(LoginType.LoginName_Password.getId(), loginTypeEnum.getId());
    }

    @Override
    public AbstractUserLoginReq collectLoginData(SecurityConfig securityConfig, HttpServletRequest request) {
        return getLoginNamePasswordReq(securityConfig, request);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    protected LoginNamePasswordReq getLoginNamePasswordReq(SecurityConfig securityConfig, HttpServletRequest request) {
        LoginConfig login = securityConfig.getLogin();
        LoginNamePasswordReq req = HttpServletRequestUtils.parseBodyToEntity(request, LoginNamePasswordReq.class);
        if (req == null && login.isPostOnly()) {
            return null;
        }
        if (req == null) {
            req = new LoginNamePasswordReq();
        }
        if (req.getRememberMe() == null) {
            String rememberMe = request.getParameter(AbstractUserLoginReq.RememberMe_ParamName);
            if (StringUtils.isNotBlank(rememberMe)) {
                req.setRememberMe(BooleanUtils.toBoolean(rememberMe));
            }
        }
        if (req.getLoginChannel() == null) {
            String loginChannel = request.getParameter(AbstractUserLoginReq.LoginChannel_ParamName);
            if (StringUtils.isNotBlank(loginChannel)) {
                LoginChannel loginChannelEnum = LoginChannel.lookup(loginChannel);
                if (loginChannelEnum != null) {
                    req.setLoginChannel(loginChannelEnum.getName());
                }
            }
        }
        if (req.getLoginName() == null) {
            String loginName = request.getParameter(LoginNamePasswordReq.LoginName_ParamName);
            if (loginName != null) {
                req.setLoginName(loginName);
            }
        }
        if (req.getPassword() == null) {
            String password = request.getParameter(LoginNamePasswordReq.Password_ParamName);
            if (password != null) {
                // TODO 密码对称加密需要解密
                req.setPassword(password);
            }
        }
        if (req.getLoginName() == null && req.getPassword() == null) {
            return null;
        }
        return req;
    }
}

