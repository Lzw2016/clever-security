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
 * 创建时间：2021/01/09 17:04 <br/>
 */
@FeignClient(
        contextId = "org.clever.security.client.RegisterSupportClient",
        name = Constant.ServerName,
        path = "/security/api",
        configuration = CleverSecurityFeignConfiguration.class
)
public interface RegisterSupportClient {
    /**
     * 获取登录名注册-验证码
     */
    @GetMapping("/login_name_register_captcha")
    GetLoginNameRegisterCaptchaRes getLoginNameRegisterCaptcha(@Validated @SpringQueryMap GetLoginNameRegisterCaptchaReq req);

    /**
     * 验证登录名注册-验证码
     */
    @PostMapping("/verify_login_name_register_captcha")
    VerifyLoginNameRegisterCaptchaRes verifyLoginNameRegisterCaptcha(@Validated @RequestBody VerifyLoginNameRegisterCaptchaReq req);

    /**
     * 获取短信注册-图片验证码
     */
    @GetMapping("/sms_register_captcha")
    GetSmsRegisterCaptchaRes getSmsRegisterCaptcha(@Validated @SpringQueryMap GetSmsRegisterCaptchaReq req);

    /**
     * 验证短信注册-图片验证码
     */
    @PostMapping("/verify_sms_register_captcha")
    VerifySmsRegisterCaptchaRes verifySmsRegisterCaptcha(@Validated @RequestBody VerifySmsRegisterCaptchaReq req);

    /**
     * 发送短信注册-短信验证码
     */
    @PostMapping("/sms_register_send_validate_code")
    SendSmsValidateCodeRes sendSmsValidateCode(@Validated @RequestBody SendSmsValidateCodeReq req);

    /**
     * 验证短信注册-短信验证码
     */
    @PostMapping("/verify_sms_register_send_validate_code")
    VerifySmsValidateCodeRes verifySmsValidateCode(@Validated @RequestBody VerifySmsValidateCodeReq req);

    /**
     * 获取邮箱注册-图片验证码
     */
    @GetMapping("/email_register_captcha")
    GetEmailRegisterCaptchaRes getEmailRegisterCaptcha(@Validated @SpringQueryMap GetEmailRegisterCaptchaReq req);

    /**
     * 验证短信注册-图片验证码
     */
    @PostMapping("/verify_email_register_captcha")
    VerifyEmailRegisterCaptchaRes verifyEmailRegisterCaptcha(@Validated @RequestBody VerifyEmailRegisterCaptchaReq req);

    /**
     * 发送邮箱注册-邮箱验证码
     */
    @PostMapping("/email_register_send_validate_code")
    SendEmailValidateCodeRes sendEmailValidateCode(@Validated @RequestBody SendEmailValidateCodeReq req);

    /**
     * 验证邮箱注册-邮箱验证码
     */
    @PostMapping("/verify_email_register_send_validate_code")
    VerifyEmailValidateCodeRes verifyEmailValidateCode(@Validated @RequestBody VerifyEmailValidateCodeReq req);

    /**
     * 根据登录名注册
     */
    @PostMapping("/register_by_login_name")
    UserRegisterRes registerByLoginName(@Validated @RequestBody RegisterByLoginNameReq req);

    /**
     * 根据短信注册
     */
    @PostMapping("/register_by_sms")
    UserRegisterRes registerBySms(@Validated @RequestBody RegisterBySmsReq req);

    /**
     * 根据邮箱注册
     */
    @PostMapping("/register_by_email")
    UserRegisterRes registerByEmail(@Validated @RequestBody RegisterByEmailReq req);

    /**
     * 新增用户注册日志
     */
    @PostMapping("/add_user_register_log")
    AddUserRegisterLogRes addUserRegisterLog(@Validated @RequestBody AddUserRegisterLogReq req);
}
