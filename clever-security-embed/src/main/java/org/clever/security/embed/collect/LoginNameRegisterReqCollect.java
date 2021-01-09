package org.clever.security.embed.collect;

import org.clever.security.RegisterType;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.utils.HttpServletRequestUtils;
import org.clever.security.model.register.AbstractUserRegisterReq;
import org.clever.security.model.register.LoginNameRegisterReq;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 18:49 <br/>
 */
public class LoginNameRegisterReqCollect extends AbstractRegisterDataCollect {
    @Override
    public boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request) {
        RegisterType registerType = getRegisterType(request);
        if (registerType == null) {
            LoginNameRegisterReq req = getLoginNameRegisterReq(securityConfig, request);
            return req != null;
        }
        return Objects.equals(RegisterType.LoginName_Password.getId(), registerType.getId());
    }

    @Override
    public AbstractUserRegisterReq collectRegisterData(SecurityConfig securityConfig, HttpServletRequest request) {
        return getLoginNameRegisterReq(securityConfig, request);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @SuppressWarnings("DuplicatedCode")
    protected LoginNameRegisterReq getLoginNameRegisterReq(SecurityConfig securityConfig, HttpServletRequest request) {
        LoginNameRegisterReq req = HttpServletRequestUtils.parseBodyToEntity(request, LoginNameRegisterReq.class);
        if (req == null) {
            req = new LoginNameRegisterReq();
        }
        // 收集基础数据
        collectBaseDataByParameter(req, request);
        // 收集当前注册类型数据
        if (req.getLoginName() == null) {
            String loginName = request.getParameter(LoginNameRegisterReq.LoginName_ParamName);
            if (loginName != null) {
                req.setLoginName(loginName);
            }
        }
        if (req.getPassword() == null) {
            String password = request.getParameter(LoginNameRegisterReq.Password_ParamName);
            if (password != null) {
                req.setPassword(password);
            }
        }
        if (req.getLoginName() == null && req.getPassword() == null) {
            return null;
        }
        return req;
    }
}
