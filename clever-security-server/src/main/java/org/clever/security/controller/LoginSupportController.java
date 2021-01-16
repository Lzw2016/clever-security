package org.clever.security.controller;

import org.clever.security.client.LoginSupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.entity.Domain;
import org.clever.security.entity.JwtToken;
import org.clever.security.entity.ScanCodeLogin;
import org.clever.security.entity.User;
import org.clever.security.model.UserInfo;
import org.clever.security.service.LoginSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/18 22:17 <br/>
 */
@RestController
@RequestMapping("/security/api")
public class LoginSupportController implements LoginSupportClient {
    @Autowired
    private LoginSupportService loginSupportService;

    /**
     * 获取域信息
     */
    @GetMapping("/domain")
    @Override
    public Domain getDomain(@Validated GetDomainReq req) {
        return loginSupportService.getDomain(req);
    }

    /**
     * 获取登录图片验证码
     */
    @GetMapping("/login_captcha")
    @Override
    public GetLoginCaptchaRes getLoginCaptcha(@Validated GetLoginCaptchaReq req) {
        return loginSupportService.getLoginCaptcha(req);
    }

    /**
     * 验证登录验证码
     */
    @GetMapping("/verify_login_captcha")
    @Override
    public VerifyLoginCaptchaRes verifyLoginCaptcha(@Validated VerifyLoginCaptchaReq req) {
        return loginSupportService.verifyLoginCaptcha(req);
    }

    /**
     * 发送邮箱登录验证码
     */
    @PostMapping("/send_login_validate_code_for_email")
    @Override
    public SendLoginValidateCodeForEmailRes sendLoginValidateCodeForEmail(@Validated @RequestBody SendLoginValidateCodeForEmailReq req) {
        return loginSupportService.sendLoginValidateCodeForEmail(req);
    }

    /**
     * 验证邮箱登录验证码
     */
    @GetMapping("/verify_login_email_validate_code")
    @Override
    public VerifyLoginEmailValidateCodeRes verifyLoginEmailValidateCode(@Validated GetLoginEmailValidateCodeReq req) {
        return loginSupportService.verifyLoginEmailValidateCode(req);
    }

    /**
     * 发送短信登录验证码
     */
    @PostMapping("/send_login_validate_code_for_sms")
    @Override
    public SendLoginValidateCodeForSmsRes sendLoginValidateCodeForSms(@Validated @RequestBody SendLoginValidateCodeForSmsReq req) {
        return loginSupportService.sendLoginValidateCodeForSms(req);
    }

    /**
     * 获取发送的手机验证码
     */
    @GetMapping("/verify_login_sms_validate_code")
    @Override
    public VerifyLoginSmsValidateCodeRes verifyLoginSmsValidateCode(@Validated VerifyLoginSmsValidateCodeReq req) {
        return loginSupportService.verifyLoginSmsValidateCode(req);
    }

    /**
     * 创建登录扫码二维码
     */
    @PostMapping("/create_login_scan_code")
    @Override
    public CreateLoginScanCodeRes createLoginScanCode(@Validated @RequestBody CreateLoginScanCodeReq req) {
        return loginSupportService.createLoginScanCode(req);
    }

    /**
     * 绑定扫码登录二维码
     */
    @PostMapping("/scan_login_scan_code")
    @Override
    public BindLoginScanCodeRes bindLoginScanCode(@Validated @RequestBody BindLoginScanCodeReq req) {
        return loginSupportService.bindLoginScanCode(req);
    }

    /**
     * 扫码登录-确认登录
     */
    @PostMapping("/confirm_login_scan_code")
    @Override
    public ConfirmLoginScanCodeRes confirmLoginScanCode(@Validated @RequestBody ConfirmLoginScanCodeReq req) {
        return loginSupportService.confirmLoginScanCode(req);
    }

    /**
     * 获取扫码登录信息
     */
    @GetMapping("/scan_code_login_info")
    @Override
    public GetScanCodeLoginInfoRes getScanCodeLoginInfo(@Validated GetScanCodeLoginInfoReq req) {
        return loginSupportService.getScanCodeLoginInfo(req);
    }

    /**
     * 回写扫码登录状态
     */
    @PostMapping("/write_back_scan_code_login")
    public ScanCodeLogin writeBackScanCodeLogin(@Validated WriteBackScanCodeLoginReq req) {
        return loginSupportService.writeBackScanCodeLogin(req);
    }

    /**
     * 指定域中是否存在用户
     */
    @GetMapping("/domain_exists_user")
    @Override
    public DomainExistsUserRes domainExistsUser(@Validated DomainExistsUserReq req) {
        return loginSupportService.domainExistsUser(req);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/user")
    @Override
    public User getUser(@Validated GetUserReq req) {
        return loginSupportService.getUser(req);
    }

    /**
     * 获取当前用户并发登录数量
     */
    @GetMapping("/concurrent_login_count")
    @Override
    public GetConcurrentLoginCountRes getConcurrentLoginCount(@Validated GetConcurrentLoginCountReq req) {
        return loginSupportService.getConcurrentLoginCount(req);
    }

    /**
     * 根据LoginName获取用户名
     */
    @GetMapping("/get_user_info_by_login_name")
    @Override
    public UserInfo getUserInfoByLoginName(@Validated GetUserInfoByLoginNameReq req) {
        return loginSupportService.getUserInfoByLoginName(req);
    }

    /**
     * 根据Telephone获取用户名
     */
    @GetMapping("/get_user_info_by_telephone")
    @Override
    public UserInfo getUserInfoByTelephone(@Validated GetUserInfoByTelephoneReq req) {
        return loginSupportService.getUserInfoByTelephone(req);
    }

    /**
     * 根据Email获取用户名
     */
    @GetMapping("/get_user_info_by_email")
    @Override
    public UserInfo getUserInfoByEmail(@Validated GetUserInfoByEmailReq req) {
        return loginSupportService.getUserInfoByEmail(req);
    }

    /**
     * 根据WechatOpenId获取用户名
     */
    @GetMapping("/get_user_info_by_wechat_open_id")
    @Override
    public UserInfo getUserInfoByWechatOpenId(@Validated GetUserInfoByWechatOpenIdReq req) {
        return loginSupportService.getUserInfoByWechatOpenId(req);
    }

    /**
     * 根据ScanCode获取用户名
     */
    @GetMapping("/get_user_info_by_scan_code")
    @Override
    public UserInfo getUserInfoByScanCode(@Validated GetUserInfoByScanCodeReq req) {
        return loginSupportService.getUserInfoByScanCode(req);
    }

    /**
     * 新增登录日志
     */
    @PostMapping("/user_login_log")
    @Override
    public AddUserLoginLogRes addUserLoginLog(@Validated @RequestBody AddUserLoginLogReq req) {
        return loginSupportService.addUserLoginLog(req);
    }

    /**
     * 增加用户连续登录失败次数
     */
    @PostMapping("/add_login_failed_count")
    @Override
    public AddLoginFailedCountRes addLoginFailedCount(@Validated @RequestBody AddLoginFailedCountReq req) {
        return loginSupportService.addLoginFailedCount(req);
    }

    /**
     * 清除用户连续登录失败次数
     */
    @PostMapping("/clear_login_failed_count")
    @Override
    public ClearLoginFailedCountRes clearLoginFailedCount(@Validated @RequestBody ClearLoginFailedCountReq req) {
        return loginSupportService.clearLoginFailedCount(req);
    }

    /**
     * 新增JWT-Token
     */
    @PostMapping("/jwt_token")
    @Override
    public AddJwtTokenRes addJwtToken(@Validated @RequestBody AddJwtTokenReq req) {
        return loginSupportService.addJwtToken(req);
    }

    /**
     * 禁用JWT-Token
     */
    @PostMapping("/disable_first_jwt_token")
    @Override
    public DisableFirstJwtTokenRes disableFirstJwtToken(DisableFirstJwtTokenReq req) {
        return loginSupportService.disableFirstJwtToken(req);
    }

    /**
     * 获取JWT-Token
     */
    @GetMapping("/jwt_token")
    @Override
    public JwtToken getJwtToken(@Validated GetJwtTokenReq req) {
        return loginSupportService.getJwtToken(req);
    }

    /**
     * 禁用JWT-Token
     */
    @PostMapping("/disable_jwt_token")
    @Override
    public JwtToken disableJwtToken(@Validated @RequestBody DisableJwtTokenReq req) {
        return loginSupportService.disableJwtToken(req);
    }

    /**
     * 使用刷新Token(刷新Token无效返回null)
     */
    @PostMapping("/use_jwt_refresh_token")
    @Override
    public JwtToken useJwtRefreshToken(@Validated @RequestBody UseJwtRefreshTokenReq req) {
        return loginSupportService.useJwtRefreshToken(req);
    }

    /**
     * 密码匹配验证
     */
    @PostMapping("/matches_password")
    @Override
    public MatchesPasswordRes matchesPassword(@Validated @RequestBody MatchesPasswordReq req) {
        return loginSupportService.matchesPassword(req);
    }
}
