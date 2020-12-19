package org.clever.security.service;

import org.clever.security.client.LoginSupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.entity.JwtToken;
import org.clever.security.entity.User;
import org.clever.security.entity.ValidateCode;
import org.clever.security.model.UserInfo;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/18 22:23 <br/>
 */
@Transactional
@Primary
@Service
public class LoginSupportService implements LoginSupportClient {

    @Override
    public GetLoginCaptchaRes getLoginCaptcha(GetLoginCaptchaReq req) {
        return null;
    }

    @Override
    public GetLoginFailedCountAndCaptchaRes getLoginFailedCountAndCaptcha(GetLoginFailedCountAndCaptchaReq req) {
        return null;
    }

    @Override
    public SendLoginValidateCodeForEmailRes sendLoginValidateCodeForEmail(SendLoginValidateCodeForEmailReq req) {
        return null;
    }

    @Override
    public SendLoginValidateCodeForSmsRes sendLoginValidateCodeForSms(SendLoginValidateCodeForSmsReq req) {
        return null;
    }

    @Override
    public CreateLoginScanCodeRes createLoginScanCode(CreateLoginScanCodeReq req) {
        return null;
    }

    @Override
    public BindLoginScanCodeRes bindLoginScanCode(BindLoginScanCodeReq req) {
        return null;
    }

    @Override
    public ConfirmLoginScanCodeRes confirmLoginScanCode(ConfirmLoginScanCodeReq req) {
        return null;
    }

    @Override
    public GetScanCodeLoginInfoRes getScanCodeLoginInfo(GetScanCodeLoginInfoReq req) {
        return null;
    }

    @Override
    public ValidateCode getLoginSmsValidateCode(GetLoginSmsValidateCodeReq req) {
        return null;
    }

    @Override
    public ValidateCode getLoginEmailValidateCode(GetLoginEmailValidateCodeReq req) {
        return null;
    }

    @Override
    public DomainExistsUserRes domainExistsUser(DomainExistsUserReq req) {
        return null;
    }

    @Override
    public User getUser(GetUserReq req) {
        return null;
    }

    @Override
    public GetConcurrentLoginCountRes getConcurrentLoginCount(GetConcurrentLoginCountReq req) {
        return null;
    }

    @Override
    public UserInfo getUserInfoByLoginName(GetUserInfoByLoginNameReq req) {
        return null;
    }

    @Override
    public UserInfo getUserInfoByTelephone(GetUserInfoByTelephoneReq req) {
        return null;
    }

    @Override
    public UserInfo getUserInfoByEmail(GetUserInfoByEmailReq req) {
        return null;
    }

    @Override
    public UserInfo getUserInfoByWechatOpenId(GetUserInfoByWechatOpenIdReq req) {
        return null;
    }

    @Override
    public UserInfo getUserInfoByScanCode(GetUserInfoByScanCodeReq req) {
        return null;
    }

    @Override
    public AddUserLoginLogRes addUserLoginLog(AddUserLoginLogReq req) {
        return null;
    }

    @Override
    public AddLoginFailedCountRes addLoginFailedCount(AddLoginFailedCountReq req) {
        return null;
    }

    @Override
    public ClearLoginFailedCountRes clearLoginFailedCount(ClearLoginFailedCountReq req) {
        return null;
    }

    @Override
    public AddJwtTokenRes addJwtToken(AddJwtTokenReq req) {
        return null;
    }

    @Override
    public DisableFirstJwtTokenRes disableFirstJwtToken(DisableFirstJwtTokenReq req) {
        return null;
    }

    @Override
    public JwtToken getJwtToken(GetJwtTokenReq req) {
        return null;
    }

    @Override
    public JwtToken disableJwtToken(DisableJwtTokenReq req) {
        return null;
    }
}
