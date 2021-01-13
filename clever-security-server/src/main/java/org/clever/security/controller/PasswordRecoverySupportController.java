package org.clever.security.controller;

import org.clever.security.client.PasswordRecoverySupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.service.PasswordRecoverySupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/12 21:26 <br/>
 */
@RestController
@RequestMapping("/security/api")
public class PasswordRecoverySupportController implements PasswordRecoverySupportClient {
    @Autowired
    private PasswordRecoverySupportService passwordRecoverySupportService;

    /**
     * 短信找回密码-图片验证码
     */
    @GetMapping("/password_recovery/sms/captcha")
    @Override
    public GetSmsRecoveryCaptchaRes getSmsRecoveryCaptcha(@Validated @SpringQueryMap GetSmsRecoveryCaptchaReq req) {
        return passwordRecoverySupportService.getSmsRecoveryCaptcha(req);
    }

    /**
     * 验证短信找回密码-图片验证码
     */
    @PostMapping("/password_recovery/sms/captcha/verify")
    @Override
    public VerifySmsRecoveryCaptchaRes verifySmsRecoveryCaptcha(@Validated @RequestBody VerifySmsRecoveryCaptchaReq req) {
        return passwordRecoverySupportService.verifySmsRecoveryCaptcha(req);
    }

    /**
     * 短信找回密码-短信验证码
     */
    @PostMapping("/password_recovery/sms/validate_code")
    @Override
    public SendSmsRecoveryValidateCodeRes sendSmsRecoveryValidateCode(@Validated @RequestBody SendSmsRecoveryValidateCodeReq req) {
        return passwordRecoverySupportService.sendSmsRecoveryValidateCode(req);
    }

    /**
     * 验证短信找回密码-短信验证码
     */
    @PostMapping("/password_recovery/sms/validate_code/verify")
    @Override
    public VerifySmsRecoveryValidateCodeRes verifySmsRecoveryValidateCode(@Validated @RequestBody VerifySmsRecoveryValidateCodeReq req) {
        return passwordRecoverySupportService.verifySmsRecoveryValidateCode(req);
    }

    /**
     * 邮箱找回密码-图片验证码
     */
    @GetMapping("/password_recovery/email/captcha")
    @Override
    public GetEmailRecoveryCaptchaRes getEmailRecoveryCaptcha(@Validated @SpringQueryMap GetEmailRecoveryCaptchaReq req) {
        return passwordRecoverySupportService.getEmailRecoveryCaptcha(req);
    }

    /**
     * 验证邮箱找回密码-图片验证码
     */
    @PostMapping("/password_recovery/email/captcha/verify")
    @Override
    public VerifyEmailRecoveryCaptchaRes verifyEmailRecoveryCaptcha(@Validated @RequestBody VerifyEmailRecoveryCaptchaReq req) {
        return passwordRecoverySupportService.verifyEmailRecoveryCaptcha(req);
    }

    /**
     * 邮箱找回密码-邮箱验证码
     */
    @PostMapping("/password_recovery/email/validate_code")
    @Override
    public SendEmailRecoveryValidateCodeRes sendEmailRecoveryValidateCode(@Validated @RequestBody SendEmailRecoveryValidateCodeReq req) {
        return passwordRecoverySupportService.sendEmailRecoveryValidateCode(req);
    }

    /**
     * 验证找回密码-邮箱验证码
     */
    @PostMapping("/password_recovery/email/validate_code/verify")
    @Override
    public VerifyEmailRecoveryValidateCodeRes verifyEmailRecoveryValidateCode(@Validated @RequestBody VerifyEmailRecoveryValidateCodeReq req) {
        return passwordRecoverySupportService.verifyEmailRecoveryValidateCode(req);
    }

    /**
     * 找回密码-重置密码
     */
    @PostMapping("/password_recovery/reset_password")
    @Override
    public ResetPasswordReqRes resetPassword(@Validated @RequestBody ResetPasswordReq req) {
        return passwordRecoverySupportService.resetPassword(req);
    }
}
