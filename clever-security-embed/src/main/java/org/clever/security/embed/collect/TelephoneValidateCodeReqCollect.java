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
import org.clever.security.model.login.TelephoneValidateCodeReq;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 收集“用户名/密码”登录数据
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:05 <br/>
 */
public class TelephoneValidateCodeReqCollect implements LoginDataCollect {
    @Override
    public boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request) {
        String loginType = request.getParameter(AbstractUserLoginReq.LoginType_ParamName);
        LoginType loginTypeEnum = LoginType.lookup(loginType);
        if (loginTypeEnum == null) {
            TelephoneValidateCodeReq req = getTelephoneValidateCodeReq(securityConfig, request);
            return req != null;
        }
        return Objects.equals(LoginType.Telephone_ValidateCode.getId(), loginTypeEnum.getId());
    }

    @Override
    public AbstractUserLoginReq collectLoginData(SecurityConfig securityConfig, HttpServletRequest request) {
        return getTelephoneValidateCodeReq(securityConfig, request);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    protected TelephoneValidateCodeReq getTelephoneValidateCodeReq(SecurityConfig securityConfig, HttpServletRequest request) {
        LoginConfig login = securityConfig.getLogin();
        TelephoneValidateCodeReq req = HttpServletRequestUtils.parseBodyToEntity(request, TelephoneValidateCodeReq.class);
        if (req == null && login.isPostOnly()) {
            return null;
        }
        if (req == null) {
            req = new TelephoneValidateCodeReq();
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
        if (req.getTelephone() == null) {
            String telephone = request.getParameter(TelephoneValidateCodeReq.Telephone_ParamName);
            if (telephone != null) {
                req.setTelephone(telephone);
            }
        }
        if (req.getValidateCode() == null) {
            String validateCode = request.getParameter(TelephoneValidateCodeReq.ValidateCode_ParamName);
            if (validateCode != null) {
                req.setValidateCode(validateCode);
            }
        }
        if (req.getTelephone() == null && req.getValidateCode() == null) {
            return null;
        }
        return req;
    }
}

