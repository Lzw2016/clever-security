package org.clever.security.client;

import org.clever.security.Constant;
import org.clever.security.client.config.CleverSecurityFeignConfiguration;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/18 20:03 <br/>
 */
@FeignClient(
        contextId = "org.clever.security.client.ResetPasswordSupportClient",
        name = Constant.ServerName,
        path = "/security/api",
        configuration = CleverSecurityFeignConfiguration.class
)
public interface UpdatePasswordSupportClient {
    /**
     * 获取设置密码-获得验证码
     */
    @GetMapping("/update_password/captcha")
    GetUpdatePasswordCaptchaRes getUpdatePasswordCaptcha(@Validated @SpringQueryMap GetUpdatePasswordCaptchaReq req);

    /**
     * 验证设置密码-验证验证码
     */
    @PostMapping("/update_password/captcha/verify")
    VerifyUpdatePasswordCaptchaRes verifyUpdatePasswordCaptcha(@Validated @RequestBody VerifyUpdatePasswordCaptchaReq req);

    /**
     * 发送设置密码-发送短信验证码
     */
    @PostMapping("/update_password/send_sms_validate_code")
    SendSmsUpdatePasswordValidateCodeRes sendSmsUpdatePasswordValidateCode(@Validated @RequestBody SendSmsUpdatePasswordValidateCodeReq req);

    /**
     * 验证设置密码-验证短信验证码
     */
    @PostMapping("/update_password/verify_sms_validate_code")
    VerifySmsUpdatePasswordValidateCodeRes verifySmsUpdatePasswordValidateCode(@Validated @RequestBody VerifySmsUpdatePasswordValidateCodeReq req);

    /**
     * 发送设置密码-发送邮箱验证码
     */
    @PostMapping("/update_password/send_email_validate_code")
    SendEmailUpdatePasswordValidateCodeRes sendEmailUpdatePasswordValidateCode(@Validated @RequestBody SendEmailUpdatePasswordValidateCodeReq req);

    /**
     * 验证设置密码-验证邮箱验证码
     */
    @PostMapping("/update_password/verify_email_validate_code")
    VerifyEmailUpdatePasswordValidateCodeRes verifyEmailUpdatePasswordValidateCode(@Validated @RequestBody VerifyEmailUpdatePasswordValidateCodeReq req);

    /**
     * 设置密码
     */
    @PostMapping("/password/init")
    InitPasswordRes initPassword(@Validated @RequestBody InitPasswordReq req);

    /**
     * 修改密码
     */
    @PostMapping("/password/update")
    UpdatePasswordRes updatePassword(@Validated @RequestBody UpdatePasswordReq req);
}
