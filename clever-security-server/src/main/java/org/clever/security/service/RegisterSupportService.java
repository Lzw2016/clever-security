package org.clever.security.service;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.RegisterSupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.model.register.EmailRegisterReq;
import org.clever.security.model.register.LoginNameRegisterReq;
import org.clever.security.model.register.SmsRegisterReq;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 17:25 <br/>
 */
@Transactional
@Primary
@Service
@Slf4j
public class RegisterSupportService implements RegisterSupportClient {

    @Override
    public GetLoginNameRegisterCaptchaRes getLoginNameRegisterCaptcha(GetLoginNameRegisterCaptchaReq req) {
        return null;
    }

    @Override
    public VerifyLoginNameRegisterCaptchaRes verifyLoginNameRegisterCaptcha(VerifyLoginNameRegisterCaptchaReq req) {
        return null;
    }

    @Override
    public GetSmsRegisterCaptchaRes getSmsRegisterCaptcha(GetSmsRegisterCaptchaReq req) {
        return null;
    }

    @Override
    public VerifySmsRegisterCaptchaRes verifySmsRegisterCaptcha(VerifySmsRegisterCaptchaReq req) {
        return null;
    }

    @Override
    public SendSmsValidateCodeRes sendSmsValidateCode(SendSmsValidateCodeReq req) {
        return null;
    }

    @Override
    public GetEmailRegisterCaptchaRes getEmailRegisterCaptcha(GetEmailRegisterCaptchaReq req) {
        return null;
    }

    @Override
    public VerifyEmailRegisterCaptchaRes verifyEmailRegisterCaptcha(VerifyEmailRegisterCaptchaReq req) {
        return null;
    }

    @Override
    public SendEmailValidateCodeRes sendEmailValidateCode(SendEmailValidateCodeReq req) {
        return null;
    }

    @Override
    public UserRegisterRes registerByLoginName(LoginNameRegisterReq req) {
        return null;
    }

    @Override
    public UserRegisterRes registerByEmail(EmailRegisterReq req) {
        return null;
    }

    @Override
    public UserRegisterRes registerBySms(SmsRegisterReq req) {
        return null;
    }
}
