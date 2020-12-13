package org.clever.security.client;

import org.clever.security.Constant;
import org.clever.security.client.config.CleverSecurityFeignConfiguration;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.entity.User;
import org.clever.security.entity.ValidateCode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/12 22:03 <br/>
 */
@FeignClient(
        contextId = "org.clever.security.client.LoginSupportClient",
        name = Constant.ServerName,
        path = "/security/api",
        configuration = CleverSecurityFeignConfiguration.class
)
public interface LoginSupportClient {
    /**
     * 获取登录图片验证码
     */
    @GetMapping("/login_captcha")
    GetLoginCaptchaRes getLoginCaptcha(GetLoginCaptchaReq req);

    /**
     * 获取用户登录失败次数和验证码信息
     */
    @GetMapping("/login_failed_count_and_captcha")
    GetLoginFailedCountAndCaptchaRes getLoginFailedCountAndCaptcha(GetLoginFailedCountAndCaptchaReq req);

    /**
     * 获取扫码登录信息
     */
    @GetMapping("/concurrent_login_count")
    GetScanCodeLoginInfoRes getScanCodeLoginInfo(GetScanCodeLoginInfoReq req);

    /**
     * 获取发送的手机验证码
     */
    @GetMapping("/login_sms_validate_code")
    ValidateCode getLoginSmsValidateCode(GetLoginSmsValidateCodeReq req);

    /**
     * 获取发送的邮箱验证码
     */
    @GetMapping("/login_email_validate_code")
    ValidateCode getLoginEmailValidateCode(GetLoginEmailValidateCodeReq req);

    /**
     * 获取用户在指定域中
     */
    @GetMapping("/domain_exists_user")
    DomainExistsUserRes domainExistsUser(DomainExistsUserReq req);

    /**
     * 获取发送的邮箱验证码
     */
    @GetMapping("/user")
    User getUser(GetUserReq req);

    /**
     * 获取当前用户并发登录数量
     */
    @GetMapping("/concurrent_login_count")
    GetConcurrentLoginCountRes getConcurrentLoginCount(GetConcurrentLoginCountReq req);


}
