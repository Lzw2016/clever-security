package org.clever.security.client;

import org.clever.security.Constant;
import org.clever.security.client.config.CleverSecurityFeignConfiguration;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.entity.User;
import org.clever.security.entity.ValidateCode;
import org.clever.security.model.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    GetLoginCaptchaRes getLoginCaptcha(@Validated GetLoginCaptchaReq req);

    /**
     * 获取用户登录失败次数和验证码信息
     */
    @GetMapping("/login_failed_count_and_captcha")
    GetLoginFailedCountAndCaptchaRes getLoginFailedCountAndCaptcha(@Validated GetLoginFailedCountAndCaptchaReq req);

    /**
     * 发送邮箱登录验证码
     */
    @PostMapping("/send_login_validate_code_for_email")
    SendLoginValidateCodeForEmailRes sendLoginValidateCodeForEmail(@Validated SendLoginValidateCodeForEmailReq req);

    /**
     * 发送邮箱登录验证码
     */
    @PostMapping("/send_login_validate_code_for_sms")
    SendLoginValidateCodeForSmsRes sendLoginValidateCodeForSms(@Validated SendLoginValidateCodeForSmsReq req);

    /**
     * 创建登录扫码二维码
     */
    @PostMapping("/create_login_scan_code")
    CreateLoginScanCodeRes createLoginScanCode(@Validated @RequestBody CreateLoginScanCodeReq req);

    /**
     * 绑定扫码登录二维码
     */
    @PostMapping("/scan_login_scan_code")
    BindLoginScanCodeRes bindLoginScanCode(@Validated @RequestBody BindLoginScanCodeReq req);

    /**
     * 扫码登录-确认登录
     */
    @PostMapping("/confirm_login_scan_code")
    ConfirmLoginScanCodeRes confirmLoginScanCode(@Validated @RequestBody ConfirmLoginScanCodeReq req);

    /**
     * 获取扫码登录信息
     */
    @GetMapping("/scan_code_login_info")
    GetScanCodeLoginInfoRes getScanCodeLoginInfo(@Validated GetScanCodeLoginInfoReq req);

    /**
     * 获取发送的手机验证码
     */
    @GetMapping("/login_sms_validate_code")
    ValidateCode getLoginSmsValidateCode(@Validated GetLoginSmsValidateCodeReq req);

    /**
     * 获取发送的邮箱验证码
     */
    @GetMapping("/login_email_validate_code")
    ValidateCode getLoginEmailValidateCode(@Validated GetLoginEmailValidateCodeReq req);

    /**
     * 获取用户在指定域中
     */
    @GetMapping("/domain_exists_user")
    DomainExistsUserRes domainExistsUser(@Validated DomainExistsUserReq req);

    /**
     * 获取发送的邮箱验证码
     */
    @GetMapping("/user")
    User getUser(@Validated GetUserReq req);

    /**
     * 获取当前用户并发登录数量
     */
    @GetMapping("/concurrent_login_count")
    GetConcurrentLoginCountRes getConcurrentLoginCount(@Validated GetConcurrentLoginCountReq req);

    /**
     * 根据LoginName获取用户名
     */
    @GetMapping("/get_user_info_by_login_name")
    UserInfo getUserInfoByLoginName(@Validated GetUserInfoByLoginNameReq req);

    /**
     * 根据Telephone获取用户名
     */
    @GetMapping("/get_user_info_by_telephone")
    UserInfo getUserInfoByTelephone(@Validated GetUserInfoByTelephoneReq req);

    /**
     * 根据Email获取用户名
     */
    @GetMapping("/get_user_info_by_email")
    UserInfo getUserInfoByEmail(@Validated GetUserInfoByEmailReq req);

    /**
     * 根据WechatOpenId获取用户名
     */
    @GetMapping("/get_user_info_by_wechat_open_id")
    UserInfo getUserInfoByWechatOpenId(@Validated GetUserInfoByWechatOpenIdReq req);

    /**
     * 根据ScanCode获取用户名
     */
    @GetMapping("/get_user_info_by_scan_code")
    UserInfo getUserInfoByScanCode(@Validated GetUserInfoByScanCodeReq req);
}
