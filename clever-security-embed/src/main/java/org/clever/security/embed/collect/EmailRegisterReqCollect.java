package org.clever.security.embed.collect;

import org.clever.security.RegisterType;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.utils.HttpServletRequestUtils;
import org.clever.security.model.register.AbstractUserRegisterReq;
import org.clever.security.model.register.EmailRegisterReq;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 18:11 <br/>
 */
public class EmailRegisterReqCollect extends AbstractRegisterDataCollect {
    @Override
    public boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request) {
        RegisterType registerType = getRegisterType(request);
        if (registerType == null) {
            EmailRegisterReq req = getEmailRegisterReq(securityConfig, request);
            return req != null;
        }
        return Objects.equals(RegisterType.Email_ValidateCode.getId(), registerType.getId());
    }

    @Override
    public AbstractUserRegisterReq collectRegisterData(SecurityConfig securityConfig, HttpServletRequest request) {
        return getEmailRegisterReq(securityConfig, request);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @SuppressWarnings("DuplicatedCode")
    protected EmailRegisterReq getEmailRegisterReq(SecurityConfig securityConfig, HttpServletRequest request) {
        EmailRegisterReq req = HttpServletRequestUtils.parseBodyToEntity(request, EmailRegisterReq.class);
        if (req == null) {
            req = new EmailRegisterReq();
        }
        // 收集基础数据
        collectBaseDataByParameter(req, request);
        // 收集当前注册类型数据
        if (req.getEmail() == null) {
            String email = request.getParameter(EmailRegisterReq.Email_ParamName);
            if (email != null) {
                req.setEmail(email);
            }
        }
        if (req.getValidateCodeDigest() == null) {
            String validateCodeDigest = request.getParameter(EmailRegisterReq.ValidateCodeDigest_ParamName);
            if (validateCodeDigest != null) {
                req.setValidateCodeDigest(validateCodeDigest);
            }
        }
        if (req.getValidateCode() == null) {
            String validateCode = request.getParameter(EmailRegisterReq.ValidateCode_ParamName);
            if (validateCode != null) {
                req.setValidateCode(validateCode);
            }
        }
        if (req.getEmail() == null) {
            return null;
        }
        return req;
    }
}
