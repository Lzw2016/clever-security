package org.clever.security.service;

import org.clever.security.client.BindSupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/17 20:46 <br/>
 */
@Transactional
@Primary
@Service
public class BindSupportService implements BindSupportClient {
    @Override
    public GetBindEmailCaptchaRes getBindEmailCaptcha(GetBindEmailCaptchaReq req) {
        return null;
    }

    @Override
    public GetBindSmsCaptchaRes getBindSmsCaptcha(GetBindSmsCaptchaReq req) {
        return null;
    }

    @Override
    public VerifyBindEmailCaptchaRes verifyBindEmailCaptcha(VerifyBindEmailCaptchaReq req) {
        return null;
    }

    @Override
    public VerifyBindSmsCaptchaRes verifyBindSmsCaptcha(VerifyBindSmsCaptchaReq req) {
        return null;
    }

    @Override
    public SendBindEmailValidateCodeRes sendBindEmailValidateCode(SendBindEmailValidateCodeReq req) {
        return null;
    }

    @Override
    public SendBindSmsValidateCodeRes sendBindSmsValidateCode(SendBindSmsValidateCodeReq req) {
        return null;
    }

    @Override
    public VerifyBindEmailValidateCodeRes verifyBindEmailValidateCode(VerifyBindEmailValidateCodeReq req) {
        return null;
    }

    @Override
    public VerifyBindSmsValidateCodeRes verifyBindSmsValidateCode(VerifyBindSmsValidateCodeReq req) {
        return null;
    }

    @Override
    public ChangeBindEmailRes changeBindEmail(ChangeBindEmailReq req) {
        return null;
    }

    @Override
    public ChangeBindSmsRes changeBindSms(ChangeBindSmsReq req) {
        return null;
    }
}
