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
public interface ResetPasswordSupportClient {
    /**
     * 获取设置密码-验证码
     */
    @GetMapping("/set_password/captcha")
    GetSetPasswordCaptchaRes getSetPasswordCaptcha(@Validated @SpringQueryMap GetSetPasswordCaptchaReq req);

    /**
     * 验证设置密码-验证码
     */
    @PostMapping("/set_password/captcha/verify")
    VerifySetPasswordCaptchaRes verifySetPasswordCaptcha(@Validated @RequestBody VerifySetPasswordCaptchaReq req);

    /**
     * 发送设置密码-短信验证码
     */
    @PostMapping("/set_password/send_sms_validate_code")
    SendSmsSetPasswordValidateCodeRes sendSmsSetPasswordValidateCode(@Validated @RequestBody SendSmsSetPasswordValidateCodeReq req);

    /**
     * 验证设置密码-短信验证码
     */
    @PostMapping("/set_password/verify_sms_validate_code")
    VerifySmsSetPasswordValidateCodeRes verifySmsSetPasswordValidateCode(@Validated @RequestBody VerifySmsSetPasswordValidateCodeReq req);

    /**
     * 发送设置密码-邮箱验证码
     */
    @PostMapping("/set_password/send_email_validate_code")
    SendEmailSetPasswordValidateCodeRes sendEmailSetPasswordValidateCode(@Validated @RequestBody SendEmailSetPasswordValidateCodeReq req);

    /**
     * 验证设置密码-邮箱验证码
     */
    @PostMapping("/set_password/verify_email_validate_code")
    VerifyEmailSetPasswordValidateCodeRes verifyEmailSetPasswordValidateCode(@Validated @RequestBody VerifyEmailSetPasswordValidateCodeReq req);

    /**
     * 设置密码
     */
    @PostMapping("/password/set_password")
    SetPasswordRes setPassword(@Validated @RequestBody SetPasswordReq req);

    /**
     * 重置密码
     */
    @PostMapping("/password/update_password")
    UpdatePasswordRes updatePassword(@Validated @RequestBody UpdatePasswordReq req);
}
