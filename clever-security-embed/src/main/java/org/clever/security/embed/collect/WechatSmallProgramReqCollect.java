package org.clever.security.embed.collect;

import org.clever.security.LoginType;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.LoginConfig;
import org.clever.security.embed.utils.HttpServletRequestUtils;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.clever.security.model.login.WechatSmallProgramReq;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 收集“微信登录code”登录数据
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:05 <br/>
 */
public class WechatSmallProgramReqCollect extends AbstractLoginDataCollect {
    @Override
    public boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request) {
        String loginType = request.getParameter(AbstractUserLoginReq.LoginType_ParamName);
        LoginType loginTypeEnum = LoginType.lookup(loginType);
        if (loginTypeEnum == null) {
            WechatSmallProgramReq req = getWechatSmallProgramReq(securityConfig, request);
            return req != null;
        }
        return Objects.equals(LoginType.WechatSmallProgram.getId(), loginTypeEnum.getId());
    }

    @Override
    public AbstractUserLoginReq collectLoginData(SecurityConfig securityConfig, HttpServletRequest request) {
        return getWechatSmallProgramReq(securityConfig, request);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    protected WechatSmallProgramReq getWechatSmallProgramReq(SecurityConfig securityConfig, HttpServletRequest request) {
        LoginConfig login = securityConfig.getLogin();
        WechatSmallProgramReq req = HttpServletRequestUtils.parseBodyToEntity(request, WechatSmallProgramReq.class);
        if (req == null && login.isPostOnly()) {
            return null;
        }
        if (req == null) {
            req = new WechatSmallProgramReq();
        }
        // 收集基础数据
        collectBaseDataByParameter(req, request);
        // 收集当前登录类型数据
        if (req.getLoginCode() == null) {
            String loginCode = request.getParameter(WechatSmallProgramReq.LoginCode_ParamName);
            if (loginCode != null) {
                req.setLoginCode(loginCode);
            }
        }
        if (req.getLoginCode() == null) {
            return null;
        }
        return req;
    }
}

