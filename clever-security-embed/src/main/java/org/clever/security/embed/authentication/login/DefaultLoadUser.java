package org.clever.security.embed.authentication.login;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.WeChatCode2SessionRes;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.LoginConfig;
import org.clever.security.embed.config.internal.WechatSmallProgramLoginConfig;
import org.clever.security.embed.exception.UnsupportedLoginTypeException;
import org.clever.security.model.UserInfo;
import org.clever.security.model.login.*;
import org.clever.security.third.client.WeChatClient;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;

/**
 * 加载用户信息
 * <p>
 * 作者：ymx <br/>
 * 创建时间：2020/12/6 17:23 <br/>
 */
@Slf4j
public class DefaultLoadUser implements LoadUser {
    private final LoginSupportClient loginSupportClient;
    private final WeChatClient wechatClient;

    public DefaultLoadUser(LoginSupportClient loginSupportClient, WeChatClient wechatClient) {
        Assert.notNull(loginSupportClient, "参数loginSupportClient不能为null");
        Assert.notNull(wechatClient, "参数wechatClient不能为null");
        this.loginSupportClient = loginSupportClient;
        this.wechatClient = wechatClient;
    }

    @Override
    public boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserLoginReq loginReq) {
        return true;
    }

    @Override
    public UserInfo loadUserInfo(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserLoginReq loginReq) {
        if (loginReq instanceof LoginNamePasswordReq) {
            return loadUser(securityConfig, request, (LoginNamePasswordReq) loginReq);
        } else if (loginReq instanceof SmsValidateCodeReq) {
            return loadUser(securityConfig, request, (SmsValidateCodeReq) loginReq);
        } else if (loginReq instanceof WechatSmallProgramReq) {
            return loadUser(securityConfig, request, (WechatSmallProgramReq) loginReq);
        } else if (loginReq instanceof EmailValidateCodeReq) {
            return loadUser(securityConfig, request, (EmailValidateCodeReq) loginReq);
        } else if (loginReq instanceof ScanCodeReq) {
            return loadUser(securityConfig, request, (ScanCodeReq) loginReq);
        } else {
            throw new UnsupportedLoginTypeException("不支持的登录类型");
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    protected UserInfo loadUser(SecurityConfig securityConfig, HttpServletRequest request, LoginNamePasswordReq loginNamePasswordReq) {
        // 根据“loginName”加载用户信息
        GetUserInfoByLoginNameReq req = new GetUserInfoByLoginNameReq();
        req.setLoginName(loginNamePasswordReq.getLoginName());
        return loginSupportClient.getUserInfoByLoginName(req);
    }

    protected UserInfo loadUser(SecurityConfig securityConfig, HttpServletRequest request, SmsValidateCodeReq smsValidateCodeReq) {
        // 根据“telephone”加载用户信息
        GetUserInfoByTelephoneReq req = new GetUserInfoByTelephoneReq();
        req.setTelephone(smsValidateCodeReq.getTelephone());
        return loginSupportClient.getUserInfoByTelephone(req);
    }

    protected UserInfo loadUser(SecurityConfig securityConfig, HttpServletRequest request, WechatSmallProgramReq wechatSmallProgramReq) {
        LoginConfig login = securityConfig.getLogin();
        if (login == null) {
            return null;
        }
        WechatSmallProgramLoginConfig wechatSmallProgramLogin = login.getWechatSmallProgramLogin();
        if (wechatSmallProgramLogin == null) {
            return null;
        }
        if (!wechatSmallProgramLogin.isEnable()
                || StringUtils.isBlank(wechatSmallProgramLogin.getAppId())
                || StringUtils.isBlank(wechatSmallProgramLogin.getAppSecret())) {
            return null;
        }
        // 根据“微信登录code”加载用户信息
        WeChatCode2SessionReq wechatcode2SessionReq = new WeChatCode2SessionReq(
                wechatSmallProgramLogin.getAppId(),
                wechatSmallProgramLogin.getAppSecret(),
                wechatSmallProgramReq.getLoginCode()
        );
        WeChatCode2SessionRes res = wechatClient.code2Session(wechatcode2SessionReq);
        log.debug("微信小程序登录结果: [{}] -> {}", WeChatClient.Code2SessionErrMsgMap.get(res.getErrCode()), res);
        GetUserInfoByWechatOpenIdReq req = new GetUserInfoByWechatOpenIdReq(securityConfig.getDomainId());
        req.setOpenId(res.getOpenId());
        req.setUnionId(req.getUnionId());
        return loginSupportClient.getUserInfoByWechatOpenId(req);
    }

    protected UserInfo loadUser(SecurityConfig securityConfig, HttpServletRequest request, EmailValidateCodeReq emailValidateCodeReq) {
        // 根据“email”加载用户信息
        GetUserInfoByEmailReq req = new GetUserInfoByEmailReq();
        req.setEmail(emailValidateCodeReq.getEmail());
        return loginSupportClient.getUserInfoByEmail(req);
    }

    protected UserInfo loadUser(SecurityConfig securityConfig, HttpServletRequest request, ScanCodeReq scanCodeReq) {
        // 根据“scanCode”加载用户信息
        GetUserInfoByScanCodeReq req = new GetUserInfoByScanCodeReq(securityConfig.getDomainId());
        req.setScanCode(scanCodeReq.getScanCode());
        return loginSupportClient.getUserInfoByScanCode(req);
    }
}
