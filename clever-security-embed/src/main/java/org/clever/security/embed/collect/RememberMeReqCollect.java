package org.clever.security.embed.collect;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.clever.security.LoginChannel;
import org.clever.security.LoginType;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.LoginConfig;
import org.clever.security.embed.utils.HttpServletRequestUtils;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.clever.security.model.login.RememberMeReq;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 收集“记住我”功能的token登录数据
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:05 <br/>
 */
public class RememberMeReqCollect implements LoginDataCollect {
    @Override
    public boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request) {
        String loginType = request.getParameter(AbstractUserLoginReq.LoginType_ParamName);
        LoginType loginTypeEnum = LoginType.lookup(loginType);
        if (loginTypeEnum == null) {
            RememberMeReq req = getRememberMeReq(securityConfig, request);
            return req != null;
        }
        return Objects.equals(LoginType.RememberMe.getId(), loginTypeEnum.getId());
    }

    @Override
    public AbstractUserLoginReq collectLoginData(SecurityConfig securityConfig, HttpServletRequest request) {
        return getRememberMeReq(securityConfig, request);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    protected RememberMeReq getRememberMeReq(SecurityConfig securityConfig, HttpServletRequest request) {
        LoginConfig login = securityConfig.getLogin();
        RememberMeReq req = HttpServletRequestUtils.parseBodyToEntity(request, RememberMeReq.class);
        if (req == null && login.isPostOnly()) {
            return null;
        }
        if (req == null) {
            req = new RememberMeReq();
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
        if (req.getRememberMeToken() == null) {
            String rememberMeToken = request.getParameter(RememberMeReq.RememberMeToken_ParamName);
            if (rememberMeToken != null) {
                req.setRememberMeToken(rememberMeToken);
            }
        }
        if (req.getRememberMeToken() == null) {
            return null;
        }
        return req;
    }
}

