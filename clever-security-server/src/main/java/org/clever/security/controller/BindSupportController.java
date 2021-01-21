package org.clever.security.controller;

import org.clever.security.client.BindSupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.service.BindSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/17 20:46 <br/>
 */
@RestController
@RequestMapping("/security/api")
public class BindSupportController implements BindSupportClient {
    @Autowired
    private BindSupportService bindSupportService;

    /**
     * 邮箱换绑 - 图片验证码
     */
    @GetMapping("/bind_email/captcha")
    @Override
    public GetBindEmailCaptchaRes getBindEmailCaptcha(@Validated GetBindEmailCaptchaReq req) {
        return bindSupportService.getBindEmailCaptcha(req);
    }

    /**
     * 手机号换绑 - 图片验证码
     */
    @GetMapping("/bind_telephone/captcha")
    @Override
    public GetBindSmsCaptchaRes getBindSmsCaptcha(@Validated GetBindSmsCaptchaReq req) {
        return bindSupportService.getBindSmsCaptcha(req);
    }

    /**
     * 邮箱换绑 - 图片验证码验证
     */
    @PostMapping("/bind_email/captcha/verify")
    @Override
    public VerifyBindEmailCaptchaRes verifyBindEmailCaptcha(@Validated @RequestBody VerifyBindEmailCaptchaReq req) {
        return bindSupportService.verifyBindEmailCaptcha(req);
    }

    /**
     * 手机号换绑 - 图片验证码验证
     */
    @PostMapping("/bind_telephone/captcha/verify")
    @Override
    public VerifyBindSmsCaptchaRes verifyBindSmsCaptcha(@Validated @RequestBody VerifyBindSmsCaptchaReq req) {
        return bindSupportService.verifyBindSmsCaptcha(req);
    }

    /**
     * 邮箱换绑 - 邮箱验证码
     */
    @PostMapping("/bind_email/validate_code")
    @Override
    public SendBindEmailValidateCodeRes sendBindEmailValidateCode(@Validated @RequestBody SendBindEmailValidateCodeReq req) {
        return bindSupportService.sendBindEmailValidateCode(req);
    }

    /**
     * 手机号换绑 - 短信验证码
     */
    @PostMapping("/bind_telephone/validate_code")
    @Override
    public SendBindSmsValidateCodeRes sendBindSmsValidateCode(@Validated @RequestBody SendBindSmsValidateCodeReq req) {
        return bindSupportService.sendBindSmsValidateCode(req);
    }

    /**
     * 邮箱换绑 - 邮箱验证码验证
     */
    @PostMapping("/bind_email/validate_code/verify")
    @Override
    public VerifyBindEmailValidateCodeRes verifyBindEmailValidateCode(@Validated @RequestBody VerifyBindEmailValidateCodeReq req) {
        return bindSupportService.verifyBindEmailValidateCode(req);
    }

    /**
     * 手机号换绑 - 短信验证码验证
     */
    @PostMapping("/bind_telephone/validate_code/verify")
    @Override
    public VerifyBindSmsValidateCodeRes verifyBindSmsValidateCode(@Validated @RequestBody VerifyBindSmsValidateCodeReq req) {
        return bindSupportService.verifyBindSmsValidateCode(req);
    }

    /**
     * 邮箱换绑 - 绑定新邮箱
     */
    @PostMapping("/bind_email/change_email")
    @Override
    public ChangeBindEmailRes changeBindEmail(@Validated @RequestBody ChangeBindEmailReq req) {
        return bindSupportService.changeBindEmail(req);
    }

    /**
     * 手机号换绑 - 绑定新手机号
     */
    @PostMapping("/bind_telephone/change_telephone")
    @Override
    public ChangeBindSmsRes changeBindSms(@Validated @RequestBody ChangeBindSmsReq req) {
        return bindSupportService.changeBindSms(req);
    }
}
