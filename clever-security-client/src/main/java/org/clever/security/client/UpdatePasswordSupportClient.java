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
        contextId = "org.clever.security.client.UpdatePasswordSupportClient",
        name = Constant.ServerName,
        path = "/security/api",
        configuration = CleverSecurityFeignConfiguration.class
)
public interface UpdatePasswordSupportClient {
    /**
     * 获取设置密码-验证码
     */
    @GetMapping("/init_password/captcha")
    GetInitPasswordCaptchaRes getInitPasswordCaptcha(@Validated @SpringQueryMap GetInitPasswordCaptchaReq req);

    /**
     * 验证设置密码-验证码
     */
    @PostMapping("/init_password/captcha/verify")
    VerifyInitPasswordCaptchaRes verifyInitPasswordCaptcha(@Validated @RequestBody VerifyInitPasswordCaptchaReq req);

    /**
     * 发送设置密码-短信验证码
     */
    @PostMapping("/init_password/send_sms_validate_code")
    SendSmsInitPasswordValidateCodeRes sendSmsInitPasswordValidateCode(@Validated @RequestBody SendSmsInitPasswordValidateCodeReq req);

    /**
     * 验证设置密码-短信验证码
     */
    @PostMapping("/init_password/verify_sms_validate_code")
    VerifySmsInitPasswordValidateCodeRes verifySmsInitPasswordValidateCode(@Validated @RequestBody VerifySmsInitPasswordValidateCodeReq req);

    /**
     * 发送设置密码-邮箱验证码
     */
    @PostMapping("/init_password/send_email_validate_code")
    SendEmailInitPasswordValidateCodeRes sendEmailInitPasswordValidateCode(@Validated @RequestBody SendEmailInitPasswordValidateCodeReq req);

    /**
     * 验证设置密码-邮箱验证码
     */
    @PostMapping("/init_password/verify_email_validate_code")
    VerifyEmailInitPasswordValidateCodeRes verifyEmailInitPasswordValidateCode(@Validated @RequestBody VerifyEmailInitPasswordValidateCodeReq req);

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
