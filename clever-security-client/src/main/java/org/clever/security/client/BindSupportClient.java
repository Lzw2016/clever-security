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
 * 创建时间：2021/01/17 20:42 <br/>
 */
@FeignClient(
        contextId = "org.clever.security.client.BindSupportClient",
        name = Constant.ServerName,
        path = "/security/api",
        configuration = CleverSecurityFeignConfiguration.class
)
public interface BindSupportClient {
    /**
     * 邮箱换绑 - 图片验证码
     */
    @GetMapping("/bind_email/captcha")
    GetBindEmailCaptchaRes getBindEmailCaptcha(@Validated @SpringQueryMap GetBindEmailCaptchaReq req);

    /**
     * 手机号换绑 - 图片验证码
     */
    @GetMapping("/bind_telephone/captcha")
    GetBindSmsCaptchaRes getBindSmsCaptcha(@Validated @SpringQueryMap GetBindSmsCaptchaReq req);

    /**
     * 邮箱换绑 - 图片验证码验证
     */
    @PostMapping("/bind_email/captcha/verify")
    VerifyBindEmailCaptchaRes verifyBindEmailCaptcha(@Validated @RequestBody VerifyBindEmailCaptchaReq req);

    /**
     * 手机号换绑 - 图片验证码验证
     */
    @PostMapping("/bind_telephone/captcha/verify")
    VerifyBindSmsCaptchaRes verifyBindSmsCaptcha(@Validated @RequestBody VerifyBindSmsCaptchaReq req);

    /**
     * 邮箱换绑 - 邮箱验证码
     */
    @PostMapping("/bind_email/validate_code")
    SendBindEmailValidateCodeRes sendBindEmailValidateCode(@Validated @RequestBody SendBindEmailValidateCodeReq req);

    /**
     * 手机号换绑 - 短信验证码
     */
    @PostMapping("/bind_telephone/validate_code")
    SendBindSmsValidateCodeRes sendBindSmsValidateCode(@Validated @RequestBody SendBindSmsValidateCodeReq req);

    /**
     * 邮箱换绑 - 邮箱验证码验证
     */
    @PostMapping("/bind_email/validate_code/verify")
    VerifyBindEmailValidateCodeRes verifyBindEmailValidateCode(@Validated @RequestBody VerifyBindEmailValidateCodeReq req);

    /**
     * 手机号换绑 - 短信验证码验证
     */
    @PostMapping("/bind_telephone/validate_code/verify")
    VerifyBindSmsValidateCodeRes verifyBindSmsValidateCode(@Validated @RequestBody VerifyBindSmsValidateCodeReq req);

    /**
     * 邮箱换绑 - 绑定新邮箱
     */
    @PostMapping("/bind_email/change_email")
    ChangeBindEmailRes changeBindEmail(@Validated @RequestBody ChangeBindEmailReq req);

    /**
     * 手机号换绑 - 绑定新手机号
     */
    @PostMapping("/bind_telephone/change_telephone")
    ChangeBindSmsRes changeBindSms(@Validated @RequestBody ChangeBindSmsReq req);
}
