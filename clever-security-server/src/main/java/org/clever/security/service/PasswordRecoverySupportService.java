package org.clever.security.service;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.PasswordRecoverySupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/12 21:27 <br/>
 */
@Transactional
@Primary
@Service
@Slf4j
public class PasswordRecoverySupportService implements PasswordRecoverySupportClient {
    @Override
    public GetSmsRecoveryCaptchaRes getSmsRecoveryCaptcha(GetSmsRecoveryCaptchaReq req) {
        return null;
    }

    @Override
    public VerifySmsRecoveryCaptchaRes verifySmsRecoveryCaptcha(VerifySmsRecoveryCaptchaReq req) {
        return null;
    }

    @Override
    public SendSmsRecoveryValidateCodeRes sendSmsRecoveryValidateCode(SendSmsRecoveryValidateCodeReq req) {
        return null;
    }

    @Override
    public VerifySmsRecoveryValidateCodeRes verifySmsRecoveryValidateCode(VerifySmsRecoveryValidateCodeReq req) {
        return null;
    }

    @Override
    public GetEmailRecoveryCaptchaRes getEmailRecoveryCaptcha(GetEmailRecoveryCaptchaReq req) {
        return null;
    }

    @Override
    public VerifyEmailRecoveryCaptchaRes verifyEmailRecoveryCaptcha(VerifyEmailRecoveryCaptchaReq req) {
        return null;
    }

    @Override
    public SendEmailRecoveryValidateCodeRes sendEmailRecoveryValidateCode(SendEmailRecoveryValidateCodeReq req) {
        return null;
    }

    @Override
    public VerifyEmailRecoveryValidateCodeRes verifyEmailRecoveryValidateCode(VerifyEmailRecoveryValidateCodeReq req) {
        return null;
    }

    @Override
    public ResetPasswordReqRes resetPassword(ResetPasswordReq req) {
        return null;
    }
}
