package org.clever.security.embed.handler;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.security.LoginChannel;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.dto.request.AddLoginFailedCountReq;
import org.clever.security.dto.request.AddUserLoginLogReq;
import org.clever.security.dto.request.WriteBackScanCodeLoginReq;
import org.clever.security.dto.response.AddLoginFailedCountRes;
import org.clever.security.dto.response.AddUserLoginLogRes;
import org.clever.security.embed.event.LoginFailureEvent;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.ScanCodeLogin;
import org.clever.security.model.UserInfo;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.clever.security.model.login.ScanCodeReq;
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
        // 扫码登录 - 回写扫码登录状态
        if (event.getLoginData() instanceof ScanCodeReq) {
            writeBackScanCodeLogin(event, (ScanCodeReq) event.getLoginData());
        }
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
        if (loginData == null || userInfo == null) {
            return;
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
        if (loginData == null || userInfo == null) {
            return;
        }
        AddLoginFailedCountReq req = new AddLoginFailedCountReq(event.getDomainId());
        req.setUid(userInfo.getUid());
        req.setLoginType(loginData.getLoginType().getId());
        AddLoginFailedCountRes res = loginSupportClient.addLoginFailedCount(req);
        log.debug("### 增加用户连续登录失败次数: {} | uid = [{}]", res.getFailedCount(), res.getUid());
    }

    protected void writeBackScanCodeLogin(LoginFailureEvent event, ScanCodeReq scanCodeReq) {
        WriteBackScanCodeLoginReq req = new WriteBackScanCodeLoginReq(event.getDomainId());
        req.setScanCode(scanCodeReq.getScanCode());
        req.setLoginTime(new Date());
        req.setScanCodeState(EnumConstant.ScanCodeLogin_ScanCodeState_4);
        req.setInvalidReason("扫码登录失败");
        ScanCodeLogin res = loginSupportClient.writeBackScanCodeLogin(req);
        if (res != null) {
            log.debug("### 登录失败回写扫码登录状态 | scanCode={} | scanCodeState={}", res.getScanCode(), res.getScanCodeState());
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
