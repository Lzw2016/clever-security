package org.clever.security.embed.collect;

import org.clever.security.RegisterType;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.utils.HttpServletRequestUtils;
import org.clever.security.model.register.AbstractUserRegisterReq;
import org.clever.security.model.register.SmsRegisterReq;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 18:56 <br/>
 */
public class SmsRegisterReqCollect extends AbstractRegisterDataCollect {
    @Override
    public boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request) {
        RegisterType registerType = getRegisterType(request);
        if (registerType == null) {
            SmsRegisterReq req = getSmsRegisterReq(securityConfig, request);
            return req != null;
        }
        return Objects.equals(RegisterType.Sms_ValidateCode.getId(), registerType.getId());
    }

    @Override
    public AbstractUserRegisterReq collectRegisterData(SecurityConfig securityConfig, HttpServletRequest request) {
        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    protected SmsRegisterReq getSmsRegisterReq(SecurityConfig securityConfig, HttpServletRequest request) {
        SmsRegisterReq req = HttpServletRequestUtils.parseBodyToEntity(request, SmsRegisterReq.class);
        if (req == null) {
            req = new SmsRegisterReq();
        }
        // 收集基础数据
        collectBaseDataByParameter(req, request);
        // 收集当前登录类型数据
        if (req.getTelephone() == null) {
            String telephone = request.getParameter(SmsRegisterReq.Telephone_ParamName);
            if (telephone != null) {
                req.setTelephone(telephone);
            }
        }
        if (req.getValidateCodeDigest() == null) {
            String validateCodeDigest = request.getParameter(SmsRegisterReq.ValidateCodeDigest_ParamName);
            if (validateCodeDigest != null) {
                req.setValidateCodeDigest(validateCodeDigest);
            }
        }
        if (req.getValidateCode() == null) {
            String validateCode = request.getParameter(SmsRegisterReq.ValidateCode_ParamName);
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
