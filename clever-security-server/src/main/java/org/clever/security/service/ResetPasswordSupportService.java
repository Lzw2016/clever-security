package org.clever.security.service;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.ResetPasswordSupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/18 20:17 <br/>
 */
@Transactional
@Primary
@Service
@Slf4j
public class ResetPasswordSupportService implements ResetPasswordSupportClient {
    @Override
    public GetInitPasswordCaptchaRes getInitPasswordCaptcha(GetInitPasswordCaptchaReq req) {
        return null;
    }

    @Override
    public VerifyInitPasswordCaptchaRes verifyInitPasswordCaptcha(VerifyInitPasswordCaptchaReq req) {
        return null;
    }

    @Override
    public SendSmsInitPasswordValidateCodeRes sendSmsInitPasswordValidateCode(SendSmsInitPasswordValidateCodeReq req) {
        return null;
    }

    @Override
    public VerifySmsInitPasswordValidateCodeRes verifySmsInitPasswordValidateCode(VerifySmsInitPasswordValidateCodeReq req) {
        return null;
    }

    @Override
    public SendEmailInitPasswordValidateCodeRes sendEmailInitPasswordValidateCode(SendEmailInitPasswordValidateCodeReq req) {
        return null;
    }

    @Override
    public VerifyEmailInitPasswordValidateCodeRes verifyEmailInitPasswordValidateCode(VerifyEmailInitPasswordValidateCodeReq req) {
        return null;
    }

    @Override
    public InitPasswordRes initPassword(InitPasswordReq req) {
        return null;
    }

    @Override
    public UpdatePasswordRes updatePassword(UpdatePasswordReq req) {
        return null;
    }
}
