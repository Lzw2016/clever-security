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
 * 创建时间：2021/01/12 21:19 <br/>
 */
@FeignClient(
        contextId = "org.clever.security.client.RegisterSupportClient",
        name = Constant.ServerName,
        path = "/security/api",
        configuration = CleverSecurityFeignConfiguration.class
)
public interface PasswordRecoverySupportClient {
    /**
     * 短信找回密码-图片验证码
     */
    @GetMapping("/password_recovery/sms/captcha")
    GetSmsRecoveryCaptchaRes getSmsRecoveryCaptcha(@Validated @SpringQueryMap GetSmsRecoveryCaptchaReq req);

    /**
     * 验证短信找回密码-图片验证码
     */
    @PostMapping("/password_recovery/sms/captcha/verify")
    VerifySmsRecoveryCaptchaRes verifySmsRecoveryCaptcha(@Validated @RequestBody VerifySmsRecoveryCaptchaReq req);

    /**
     * 短信找回密码-短信验证码
     */
    @PostMapping("/password_recovery/sms/validate_code")
    SendSmsRecoveryValidateCodeRes sendSmsRecoveryValidateCode(@Validated @RequestBody SendSmsRecoveryValidateCodeReq req);

    /**
     * 验证短信找回密码-短信验证码
     */
    @PostMapping("/password_recovery/sms/validate_code/verify")
    VerifySmsRecoveryValidateCodeRes verifySmsRecoveryValidateCode(@Validated @RequestBody VerifySmsRecoveryValidateCodeReq req);

    /**
     * 邮箱找回密码-图片验证码
     */
    @GetMapping("/password_recovery/email/captcha")
    GetEmailRecoveryCaptchaRes getEmailRecoveryCaptcha(@Validated @SpringQueryMap GetEmailRecoveryCaptchaReq req);

    /**
     * 验证邮箱找回密码-图片验证码
     */
    @PostMapping("/password_recovery/email/captcha/verify")
    VerifyEmailRecoveryCaptchaRes verifyEmailRecoveryCaptcha(@Validated @RequestBody VerifyEmailRecoveryCaptchaReq req);

    /**
     * 邮箱找回密码-邮箱验证码
     */
    @PostMapping("/password_recovery/email/validate_code")
    SendEmailRecoveryValidateCodeRes sendEmailRecoveryValidateCode(@Validated @RequestBody SendEmailRecoveryValidateCodeReq req);

    /**
     * 验证找回密码-邮箱验证码
     */
    @PostMapping("/password_recovery/email/validate_code/verify")
    VerifyEmailRecoveryValidateCodeRes verifyEmailRecoveryValidateCode(@Validated @RequestBody VerifyEmailRecoveryValidateCodeReq req);

    /**
     * 找回密码-重置密码
     */
    @PostMapping("/password_recovery/reset_password")
    ResetPasswordReqRes resetPassword(@Validated @RequestBody ResetPasswordReq req);
}
