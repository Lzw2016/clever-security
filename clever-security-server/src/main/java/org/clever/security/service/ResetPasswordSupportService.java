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
    public GetSetPasswordCaptchaRes getSetPasswordCaptcha(GetSetPasswordCaptchaReq req) {
        return null;
    }

    @Override
    public VerifySetPasswordCaptchaRes verifySetPasswordCaptcha(VerifySetPasswordCaptchaReq req) {
        return null;
    }

    @Override
    public SendSmsSetPasswordValidateCodeRes sendSmsSetPasswordValidateCode(SendSmsSetPasswordValidateCodeReq req) {
        return null;
    }

    @Override
    public VerifySmsSetPasswordValidateCodeRes verifySmsSetPasswordValidateCode(VerifySmsSetPasswordValidateCodeReq req) {
        return null;
    }

    @Override
    public SendEmailSetPasswordValidateCodeRes sendEmailSetPasswordValidateCode(SendEmailSetPasswordValidateCodeReq req) {
        return null;
    }

    @Override
    public VerifyEmailSetPasswordValidateCodeRes verifyEmailSetPasswordValidateCode(VerifyEmailSetPasswordValidateCodeReq req) {
        return null;
    }

    @Override
    public SetPasswordRes setPassword(SetPasswordReq req) {
        return null;
    }

    @Override
    public UpdatePasswordRes updatePassword(UpdatePasswordReq req) {
        return null;
    }
}
