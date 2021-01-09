package org.clever.security.embed.collect;

import org.clever.security.LoginType;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.LoginConfig;
import org.clever.security.embed.utils.HttpServletRequestUtils;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.clever.security.model.login.SmsValidateCodeReq;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 收集“手机号/手机验证码”登录数据
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:05 <br/>
 */
public class SmsValidateCodeReqCollect extends AbstractLoginDataCollect {
    @Override
    public boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request) {
        LoginType loginType = getLoginType(request);
        if (loginType == null) {
            SmsValidateCodeReq req = getSmsValidateCodeReq(securityConfig, request);
            return req != null;
        }
        return Objects.equals(LoginType.Sms_ValidateCode.getId(), loginType.getId());
    }

    @Override
    public AbstractUserLoginReq collectLoginData(SecurityConfig securityConfig, HttpServletRequest request) {
        return getSmsValidateCodeReq(securityConfig, request);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    protected SmsValidateCodeReq getSmsValidateCodeReq(SecurityConfig securityConfig, HttpServletRequest request) {
        LoginConfig login = securityConfig.getLogin();
        SmsValidateCodeReq req = HttpServletRequestUtils.parseBodyToEntity(request, SmsValidateCodeReq.class);
        if (req == null && login.isPostOnly()) {
            return null;
        }
        if (req == null) {
            req = new SmsValidateCodeReq();
        }
        // 收集基础数据
        collectBaseDataByParameter(req, request);
        // 收集当前登录类型数据
        if (req.getTelephone() == null) {
            String telephone = request.getParameter(SmsValidateCodeReq.Telephone_ParamName);
            if (telephone != null) {
                req.setTelephone(telephone);
            }
        }
        if (req.getValidateCodeDigest() == null) {
            String validateCodeDigest = request.getParameter(SmsValidateCodeReq.ValidateCodeDigest_ParamName);
            if (validateCodeDigest != null) {
                req.setValidateCodeDigest(validateCodeDigest);
            }
        }
        if (req.getValidateCode() == null) {
            String validateCode = request.getParameter(SmsValidateCodeReq.ValidateCode_ParamName);
            if (validateCode != null) {
                req.setValidateCode(validateCode);
            }
        }
        if (req.getTelephone() == null) {
            return null;
        }
        return req;
    }
}

