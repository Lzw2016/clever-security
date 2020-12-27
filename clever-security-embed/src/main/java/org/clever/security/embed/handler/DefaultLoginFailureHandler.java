package org.clever.security.embed.handler;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.security.LoginChannel;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.AddLoginFailedCountRes;
import org.clever.security.dto.response.AddUserLoginLogRes;
import org.clever.security.embed.event.LoginFailureEvent;
import org.clever.security.entity.EnumConstant;
import org.clever.security.model.UserInfo;
import org.clever.security.model.login.*;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/01 22:28 <br/>
 */
@Slf4j
public class DefaultLoginFailureHandler implements LoginFailureHandler {
    private final LoginSupportClient loginSupportClient;

    public DefaultLoginFailureHandler(LoginSupportClient loginSupportClient) {
        Assert.notNull(loginSupportClient, "参数loginSupportClient不能为null");
        this.loginSupportClient = loginSupportClient;
    }

    @Override
    public void onLoginFailure(HttpServletRequest request, HttpServletResponse response, LoginFailureEvent event) {
        // 记录登录失败日志
        addUserLoginLog(request, event);
        // 增加连续登录失败次数
        addLoginFailedCount(event);
    }

    @SuppressWarnings("DuplicatedCode")
    protected void addUserLoginLog(HttpServletRequest request, LoginFailureEvent event) {
        // 记录登录失败日志user_login_log
        AbstractUserLoginReq loginData = event.getLoginData();
        UserInfo userInfo = event.getUserInfo();
        if (loginData == null) {
            return;
        }
        if (userInfo == null) {
            userInfo = getLoginUniqueName(loginData, event.getDomainId());
            if (userInfo == null) {
                return;
            }
        }
        AddUserLoginLogReq req = new AddUserLoginLogReq(event.getDomainId());
        req.setUid(userInfo.getUid());
        req.setLoginTime(new Date());
        req.setLoginIp(request.getRemoteAddr());
        LoginChannel loginChannel = LoginChannel.lookup(loginData.getLoginChannel());
        if (loginChannel != null) {
            req.setLoginChannel(loginChannel.getId());
        }
        req.setLoginType(loginData.getLoginType().getId());
        req.setLoginState(EnumConstant.UserLoginLog_LoginState_0);
        req.setRequestData(JacksonMapper.getInstance().toJson(loginData));
        AddUserLoginLogRes res = loginSupportClient.addUserLoginLog(req);
        log.debug("### 登录失败 -> LoginTime={} | LoginIp={}", res.getLoginTime(), res.getLoginIp());
    }

    protected void addLoginFailedCount(LoginFailureEvent event) {
        AbstractUserLoginReq loginData = event.getLoginData();
        UserInfo userInfo = event.getUserInfo();
        if (loginData == null) {
            return;
        }
        if (userInfo == null) {
            userInfo = getLoginUniqueName(loginData, event.getDomainId());
            if (userInfo == null) {
                return;
            }
        }
        AddLoginFailedCountReq req = new AddLoginFailedCountReq(event.getDomainId());
        req.setUid(userInfo.getUid());
        req.setLoginType(loginData.getLoginType().getId());
        AddLoginFailedCountRes res = loginSupportClient.addLoginFailedCount(req);
        log.debug("### 增加用户连续登录失败次数: {} | uid = [{}]", res.getFailedCount(), res.getUid());
    }

    protected UserInfo getLoginUniqueName(AbstractUserLoginReq loginData, Long domainId) {
        // 获取特定的特定的登录方式用户数据
        String loginUniqueName;
        if (loginData instanceof LoginNamePasswordReq) {
            //根据LoginName获取用户信息
            loginUniqueName = ((LoginNamePasswordReq) loginData).getLoginName();
            GetUserInfoByLoginNameReq req = new GetUserInfoByLoginNameReq();
            req.setLoginName(loginUniqueName);
            return loginSupportClient.getUserInfoByLoginName(req);
        } else if (loginData instanceof SmsValidateCodeReq) {
            //根据Telephone获取用户信息
            loginUniqueName = ((SmsValidateCodeReq) loginData).getTelephone();
            GetUserInfoByTelephoneReq req = new GetUserInfoByTelephoneReq();
            req.setTelephone(loginUniqueName);
            return loginSupportClient.getUserInfoByTelephone(req);
        } else if (loginData instanceof EmailValidateCodeReq) {
            //根据Email获取用户信息
            loginUniqueName = ((EmailValidateCodeReq) loginData).getEmail();
            GetUserInfoByEmailReq req = new GetUserInfoByEmailReq();
            req.setEmail(loginUniqueName);
            return loginSupportClient.getUserInfoByEmail(req);
        } else if (loginData instanceof WechatSmallProgramReq) {
            //根据WechatOpenId获取用户信息  todo  openId and unionId
            loginUniqueName = ((WechatSmallProgramReq) loginData).getLoginCode();
            GetUserInfoByWechatOpenIdReq req = new GetUserInfoByWechatOpenIdReq(domainId);
            req.setOpenId(loginUniqueName);
            return loginSupportClient.getUserInfoByWechatOpenId(req);
        } else if (loginData instanceof ScanCodeReq) {
            loginUniqueName = ((ScanCodeReq) loginData).getScanCode();
            GetUserInfoByScanCodeReq req = new GetUserInfoByScanCodeReq(domainId);
            req.setScanCode(loginUniqueName);
            return loginSupportClient.getUserInfoByScanCode(req);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
