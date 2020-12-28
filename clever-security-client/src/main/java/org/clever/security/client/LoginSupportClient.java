package org.clever.security.client;

import org.clever.security.Constant;
import org.clever.security.client.config.CleverSecurityFeignConfiguration;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.entity.*;
import org.clever.security.model.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
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
     * 获取域信息
     */
    @GetMapping("/domain")
    Domain getDomain(@Validated @SpringQueryMap GetDomainReq req);

    /**
     * 获取登录图片验证码
     */
    @GetMapping("/login_captcha")
    GetLoginCaptchaRes getLoginCaptcha(@Validated @SpringQueryMap GetLoginCaptchaReq req);

    /**
     * 获取用户登录失败次数和验证码信息
     */
    @GetMapping("/login_failed_count_and_captcha")
    GetLoginFailedCountAndCaptchaRes getLoginFailedCountAndCaptcha(@Validated @SpringQueryMap GetLoginFailedCountAndCaptchaReq req);

    /**
     * 发送邮箱登录验证码
     */
    @PostMapping("/send_login_validate_code_for_email")
    SendLoginValidateCodeForEmailRes sendLoginValidateCodeForEmail(@Validated @RequestBody SendLoginValidateCodeForEmailReq req);

    /**
     * 发送短信登录验证码
     */
    @PostMapping("/send_login_validate_code_for_sms")
    SendLoginValidateCodeForSmsRes sendLoginValidateCodeForSms(@Validated @RequestBody SendLoginValidateCodeForSmsReq req);

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
    GetScanCodeLoginInfoRes getScanCodeLoginInfo(@Validated @SpringQueryMap GetScanCodeLoginInfoReq req);

    /**
     * 回写扫码登录状态
     */
    @PostMapping("/write_back_scan_code_login")
    ScanCodeLogin writeBackScanCodeLogin(@Validated @SpringQueryMap WriteBackScanCodeLoginReq req);

    /**
     * 获取发送的手机验证码
     */
    @GetMapping("/login_sms_validate_code")
    ValidateCode getLoginSmsValidateCode(@Validated @SpringQueryMap GetLoginSmsValidateCodeReq req);

    /**
     * 获取发送的邮箱验证码
     */
    @GetMapping("/login_email_validate_code")
    ValidateCode getLoginEmailValidateCode(@Validated @SpringQueryMap GetLoginEmailValidateCodeReq req);

    /**
     * 获取用户在指定域中
     */
    @GetMapping("/domain_exists_user")
    DomainExistsUserRes domainExistsUser(@Validated @SpringQueryMap DomainExistsUserReq req);

    /**
     * 获取用户信息
     */
    @GetMapping("/user")
    User getUser(@Validated @SpringQueryMap GetUserReq req);

    /**
     * 获取当前用户并发登录数量
     */
    @GetMapping("/concurrent_login_count")
    GetConcurrentLoginCountRes getConcurrentLoginCount(@Validated @SpringQueryMap GetConcurrentLoginCountReq req);

    /**
     * 根据LoginName获取用户信息
     */
    @GetMapping("/get_user_info_by_login_name")
    UserInfo getUserInfoByLoginName(@Validated @SpringQueryMap GetUserInfoByLoginNameReq req);

    /**
     * 根据Telephone获取用户信息
     */
    @GetMapping("/get_user_info_by_telephone")
    UserInfo getUserInfoByTelephone(@Validated @SpringQueryMap GetUserInfoByTelephoneReq req);

    /**
     * 根据Email获取用户信息
     */
    @GetMapping("/get_user_info_by_email")
    UserInfo getUserInfoByEmail(@Validated @SpringQueryMap GetUserInfoByEmailReq req);

    /**
     * 根据WechatOpenId获取用户信息
     */
    @GetMapping("/get_user_info_by_wechat_open_id")
    UserInfo getUserInfoByWechatOpenId(@Validated @SpringQueryMap GetUserInfoByWechatOpenIdReq req);

    /**
     * 根据ScanCode获取用户信息
     */
    @GetMapping("/get_user_info_by_scan_code")
    UserInfo getUserInfoByScanCode(@Validated @SpringQueryMap GetUserInfoByScanCodeReq req);

    /**
     * 新增登录日志
     */
    @PostMapping("/user_login_log")
    AddUserLoginLogRes addUserLoginLog(@Validated @RequestBody AddUserLoginLogReq req);

    /**
     * 增加用户连续登录失败次数
     */
    @PostMapping("/add_login_failed_count")
    AddLoginFailedCountRes addLoginFailedCount(@Validated @RequestBody AddLoginFailedCountReq req);

    /**
     * 清除用户连续登录失败次数
     */
    @PostMapping("/clear_login_failed_count")
    ClearLoginFailedCountRes clearLoginFailedCount(@Validated @RequestBody ClearLoginFailedCountReq req);

    /**
     * 新增JWT-Token
     */
    @PostMapping("/jwt_token")
    AddJwtTokenRes addJwtToken(@Validated @RequestBody AddJwtTokenReq req);

    /**
     * 禁用JWT-Token
     */
    @PostMapping("/disable_first_jwt_token")
    DisableFirstJwtTokenRes disableFirstJwtToken(@Validated @RequestBody DisableFirstJwtTokenReq req);

    /**
     * 获取JWT-Token
     */
    @GetMapping("/jwt_token")
    JwtToken getJwtToken(@Validated @SpringQueryMap GetJwtTokenReq req);

    /**
     * 禁用JWT-Token
     */
    @PostMapping("/disable_jwt_token")
    JwtToken disableJwtToken(@Validated @RequestBody DisableJwtTokenReq req);

    /**
     * 使用刷新Token(刷新Token无效返回null)
     */
    @PostMapping("/use_jwt_refresh_token")
    JwtToken useJwtRefreshToken(@Validated @RequestBody UseJwtRefreshTokenReq req);
}
