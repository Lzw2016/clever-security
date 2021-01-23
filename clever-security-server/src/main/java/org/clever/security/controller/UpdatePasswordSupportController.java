package org.clever.security.controller;

import org.clever.security.client.UpdatePasswordSupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.service.UpdatePasswordSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/18 20:14 <br/>
 */
@RestController
@RequestMapping("/security/api")
public class UpdatePasswordSupportController implements UpdatePasswordSupportClient {
    @Autowired
    private UpdatePasswordSupportService updatePasswordSupportService;

    /**
     * 获取设置密码-验证码
     */
    @GetMapping("/update_password/captcha")
    @Override
    public GetUpdatePasswordCaptchaRes getUpdatePasswordCaptcha(@Validated GetUpdatePasswordCaptchaReq req) {
        return updatePasswordSupportService.getUpdatePasswordCaptcha(req);
    }

    /**
     * 验证设置密码-验证码
     */
    @PostMapping("/update_password/captcha/verify")
    @Override
    public VerifyUpdatePasswordCaptchaRes verifyUpdatePasswordCaptcha(@Validated @RequestBody VerifyUpdatePasswordCaptchaReq req) {
        return updatePasswordSupportService.verifyUpdatePasswordCaptcha(req);
    }

    /**
     * 发送设置密码-短信验证码
     */
    @PostMapping("/update_password/send_sms_validate_code")
    @Override
    public SendSmsUpdatePasswordValidateCodeRes sendSmsUpdatePasswordValidateCode(@Validated @RequestBody SendSmsUpdatePasswordValidateCodeReq req) {
        return updatePasswordSupportService.sendSmsUpdatePasswordValidateCode(req);
    }

    /**
     * 验证设置密码-短信验证码
     */
    @PostMapping("/update_password/verify_sms_validate_code")
    @Override
    public VerifySmsUpdatePasswordValidateCodeRes verifySmsUpdatePasswordValidateCode(@Validated @RequestBody VerifySmsUpdatePasswordValidateCodeReq req) {
        return updatePasswordSupportService.verifySmsUpdatePasswordValidateCode(req);
    }

    /**
     * 发送设置密码-邮箱验证码
     */
    @PostMapping("/update_password/send_email_validate_code")
    @Override
    public SendEmailUpdatePasswordValidateCodeRes sendEmailUpdatePasswordValidateCode(@Validated @RequestBody SendEmailUpdatePasswordValidateCodeReq req) {
        return updatePasswordSupportService.sendEmailUpdatePasswordValidateCode(req);
    }

    /**
     * 验证设置密码-邮箱验证码
     */
    @PostMapping("/update_password/verify_email_validate_code")
    @Override
    public VerifyEmailUpdatePasswordValidateCodeRes verifyEmailUpdatePasswordValidateCode(@Validated @RequestBody VerifyEmailUpdatePasswordValidateCodeReq req) {
        return updatePasswordSupportService.verifyEmailUpdatePasswordValidateCode(req);
    }

    /**
     * 设置密码
     */
    @PostMapping("/password/init")
    @Override
    public InitPasswordRes initPassword(@Validated @RequestBody InitPasswordReq req) {
        return updatePasswordSupportService.initPassword(req);
    }

    /**
     * 修改密码
     */
    @PostMapping("/password/update")
    @Override
    public UpdatePasswordRes updatePassword(@Validated @RequestBody UpdatePasswordReq req) {
        return updatePasswordSupportService.updatePassword(req);
    }
}
