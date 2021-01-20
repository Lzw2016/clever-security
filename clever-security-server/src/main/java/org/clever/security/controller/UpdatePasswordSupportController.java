package org.clever.security.controller;

import org.clever.security.client.UpdatePasswordSupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.service.UpdatePasswordSupportService;
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
public class UpdatePasswordSupportController implements UpdatePasswordSupportClient {
    @Autowired
    private UpdatePasswordSupportService updatePasswordSupportService;

    /**
     * 获取设置密码-验证码
     */
    @GetMapping("/init_password/captcha")
    @Override
    public GetInitPasswordCaptchaRes getInitPasswordCaptcha(@Validated @SpringQueryMap GetInitPasswordCaptchaReq req) {
        return updatePasswordSupportService.getInitPasswordCaptcha(req);
    }

    /**
     * 验证设置密码-验证码
     */
    @PostMapping("/init_password/captcha/verify")
    @Override
    public VerifyInitPasswordCaptchaRes verifyInitPasswordCaptcha(@Validated @RequestBody VerifyInitPasswordCaptchaReq req) {
        return updatePasswordSupportService.verifyInitPasswordCaptcha(req);
    }

    /**
     * 发送设置密码-短信验证码
     */
    @PostMapping("/init_password/send_sms_validate_code")
    @Override
    public SendSmsInitPasswordValidateCodeRes sendSmsInitPasswordValidateCode(@Validated @RequestBody SendSmsInitPasswordValidateCodeReq req) {
        return updatePasswordSupportService.sendSmsInitPasswordValidateCode(req);
    }

    /**
     * 验证设置密码-短信验证码
     */
    @PostMapping("/init_password/verify_sms_validate_code")
    @Override
    public VerifySmsInitPasswordValidateCodeRes verifySmsInitPasswordValidateCode(@Validated @RequestBody VerifySmsInitPasswordValidateCodeReq req) {
        return updatePasswordSupportService.verifySmsInitPasswordValidateCode(req);
    }

    /**
     * 发送设置密码-邮箱验证码
     */
    @PostMapping("/init_password/send_email_validate_code")
    @Override
    public SendEmailInitPasswordValidateCodeRes sendEmailInitPasswordValidateCode(@Validated @RequestBody SendEmailInitPasswordValidateCodeReq req) {
        return updatePasswordSupportService.sendEmailInitPasswordValidateCode(req);
    }

    /**
     * 验证设置密码-邮箱验证码
     */
    @PostMapping("/init_password/verify_email_validate_code")
    @Override
    public VerifyEmailInitPasswordValidateCodeRes verifyEmailInitPasswordValidateCode(@Validated @RequestBody VerifyEmailInitPasswordValidateCodeReq req) {
        return updatePasswordSupportService.verifyEmailInitPasswordValidateCode(req);
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
