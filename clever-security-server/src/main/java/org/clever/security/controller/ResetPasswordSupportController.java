package org.clever.security.controller;

import org.clever.security.client.ResetPasswordSupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.service.ResetPasswordSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/18 20:14 <br/>
 */
@RestController
@RequestMapping("/security/api")
public class ResetPasswordSupportController implements ResetPasswordSupportClient {
    @Autowired
    private ResetPasswordSupportService resetPasswordSupportService;

    /**
     * 获取设置密码-验证码
     */
    @GetMapping("/set_password/captcha")
    @Override
    public GetSetPasswordCaptchaRes getSetPasswordCaptcha(@Validated @SpringQueryMap GetSetPasswordCaptchaReq req) {
        return resetPasswordSupportService.getSetPasswordCaptcha(req);
    }

    /**
     * 验证设置密码-验证码
     */
    @PostMapping("/set_password/captcha/verify")
    @Override
    public VerifySetPasswordCaptchaRes verifySetPasswordCaptcha(@Validated @RequestBody VerifySetPasswordCaptchaReq req) {
        return resetPasswordSupportService.verifySetPasswordCaptcha(req);
    }

    /**
     * 发送设置密码-短信验证码
     */
    @PostMapping("/set_password/send_sms_validate_code")
    @Override
    public SendSmsSetPasswordValidateCodeRes sendSmsSetPasswordValidateCode(@Validated @RequestBody SendSmsSetPasswordValidateCodeReq req) {
        return resetPasswordSupportService.sendSmsSetPasswordValidateCode(req);
    }

    /**
     * 验证设置密码-短信验证码
     */
    @PostMapping("/set_password/verify_sms_validate_code")
    @Override
    public VerifySmsSetPasswordValidateCodeRes verifySmsSetPasswordValidateCode(@Validated @RequestBody VerifySmsSetPasswordValidateCodeReq req) {
        return resetPasswordSupportService.verifySmsSetPasswordValidateCode(req);
    }

    /**
     * 发送设置密码-邮箱验证码
     */
    @PostMapping("/set_password/send_email_validate_code")
    @Override
    public SendEmailSetPasswordValidateCodeRes sendEmailSetPasswordValidateCode(@Validated @RequestBody SendEmailSetPasswordValidateCodeReq req) {
        return resetPasswordSupportService.sendEmailSetPasswordValidateCode(req);
    }

    /**
     * 验证设置密码-邮箱验证码
     */
    @PostMapping("/set_password/verify_email_validate_code")
    @Override
    public VerifyEmailSetPasswordValidateCodeRes verifyEmailSetPasswordValidateCode(@Validated @RequestBody VerifyEmailSetPasswordValidateCodeReq req) {
        return resetPasswordSupportService.verifyEmailSetPasswordValidateCode(req);
    }

    /**
     * 设置密码
     */
    @PostMapping("/password/set_password")
    @Override
    public SetPasswordRes setPassword(@Validated @RequestBody SetPasswordReq req) {
        return resetPasswordSupportService.setPassword(req);
    }

    /**
     * 重置密码
     */
    @PostMapping("/password/update_password")
    @Override
    public UpdatePasswordRes updatePassword(@Validated @RequestBody UpdatePasswordReq req) {
        return resetPasswordSupportService.updatePassword(req);
    }
}
