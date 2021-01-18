package org.clever.security.controller;

import org.clever.security.client.BindSupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.service.BindSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/17 20:46 <br/>
 */
@RestController
@RequestMapping("/security/api")
public class BindSupportController implements BindSupportClient {
    @Autowired
    private BindSupportService bindSupportService;

    @Override
    public GetBindEmailCaptchaRes getBindEmailCaptcha(GetBindEmailCaptchaReq req) {
        return bindSupportService.getBindEmailCaptcha(req);
    }

    @Override
    public GetBindSmsCaptchaRes getBindSmsCaptcha(GetBindSmsCaptchaReq req) {
        return bindSupportService.getBindSmsCaptcha(req);
    }

    @Override
    public VerifyBindEmailCaptchaRes verifyBindEmailCaptcha(VerifyBindEmailCaptchaReq req) {
        return bindSupportService.verifyBindEmailCaptcha(req);
    }

    @Override
    public VerifyBindSmsCaptchaRes verifyBindSmsCaptcha(VerifyBindSmsCaptchaReq req) {
        return bindSupportService.verifyBindSmsCaptcha(req);
    }

    @Override
    public SendBindEmailValidateCodeRes sendBindEmailValidateCode(SendBindEmailValidateCodeReq req) {
        return bindSupportService.sendBindEmailValidateCode(req);
    }

    @Override
    public SendBindSmsValidateCodeRes sendBindSmsValidateCode(SendBindSmsValidateCodeReq req) {
        return bindSupportService.sendBindSmsValidateCode(req);
    }

    @Override
    public VerifyBindEmailValidateCodeRes verifyBindEmailValidateCode(VerifyBindEmailValidateCodeReq req) {
        return bindSupportService.verifyBindEmailValidateCode(req);
    }

    @Override
    public VerifyBindSmsValidateCodeRes verifyBindSmsValidateCode(VerifyBindSmsValidateCodeReq req) {
        return bindSupportService.verifyBindSmsValidateCode(req);
    }

    @Override
    public ChangeBindEmailRes changeBindEmail(ChangeBindEmailReq req) {
        return bindSupportService.changeBindEmail(req);
    }

    @Override
    public ChangeBindSmsRes changeBindSms(ChangeBindSmsReq req) {
        return bindSupportService.changeBindSms(req);
    }
}
