package org.clever.security.embed.handler;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.security.LoginChannel;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.embed.config.internal.LoginConfig;
import org.clever.security.embed.event.LoginSuccessEvent;
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
 * 创建时间：2020/12/01 22:31 <br/>
 */
@Slf4j
public class DefaultLoginSuccessHandler implements LoginSuccessHandler {
    private final LoginSupportClient loginSupportClient;

    public DefaultLoginSuccessHandler(LoginSupportClient loginSupportClient) {
        Assert.notNull(loginSupportClient, "参数loginSupportClient不能为null");
        this.loginSupportClient = loginSupportClient;
    }

    @Override
    public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, LoginSuccessEvent event) {
        // 保存JWT-Token
        long jwtTokenId = addJwtToken(event);
        // 扫码登录 - 回写扫码登录状态
        if (event.getLoginData() instanceof ScanCodeReq) {
            writeBackScanCodeLogin(event, (ScanCodeReq) event.getLoginData());
        }
        // 记录登录成功日志
        addUserLoginLog(jwtTokenId, request, event);
        // 清除连续登录失败次数
        clearLoginFailedCount(event);
        // 挤下最早登录的用户
        disableFirstToken(event);
    }

    protected Long addJwtToken(LoginSuccessEvent event) {
        UserInfo userInfo = event.getUserInfo();
        String jwtToken = event.getJwtToken();
        Claims claims = event.getClaims();
        Assert.notNull(userInfo, "userInfo不能为null");
        Assert.hasText(jwtToken, "jwtToken不能为空");
        Assert.notNull(claims, "claims不能为null");
        AddJwtTokenReq req = new AddJwtTokenReq(event.getDomainId());
        req.setId(Long.parseLong(claims.getId()));
        req.setUid(userInfo.getUid());
        req.setToken(jwtToken);
        req.setExpiredTime(claims.getExpiration());
        req.setRefreshToken(event.getRefreshToken());
        req.setRefreshTokenExpiredTime(event.getRefreshTokenExpiredTime());
        AddJwtTokenRes res = loginSupportClient.addJwtToken(req);
        log.debug("### JWT-Token保存成功 -> Id={} | ExpiredTime={}", res.getId(), res.getExpiredTime());
        return res.getId();
    }

    @SuppressWarnings("DuplicatedCode")
    protected void addUserLoginLog(long jwtTokenId, HttpServletRequest request, LoginSuccessEvent event) {
        // 记录登录成功日志user_login_log
        AbstractUserLoginReq loginData = event.getLoginData();
        UserInfo userInfo = event.getUserInfo();
        Assert.notNull(loginData, "loginData不能为null");
        Assert.notNull(userInfo, "userInfo不能为null");
        AddUserLoginLogReq req = new AddUserLoginLogReq(event.getDomainId());
        req.setJwtTokenId(jwtTokenId);
        req.setUid(userInfo.getUid());
        req.setLoginTime(new Date());
        req.setLoginIp(request.getRemoteAddr());
        LoginChannel loginChannel = LoginChannel.lookup(loginData.getLoginChannel());
        if (loginChannel != null) {
            req.setLoginChannel(loginChannel.getId());
        }
        req.setLoginType(loginData.getLoginType().getId());
        req.setLoginState(EnumConstant.UserLoginLog_LoginState_1);
        req.setRequestData(JacksonMapper.getInstance().toJson(loginData));
        AddUserLoginLogRes res = loginSupportClient.addUserLoginLog(req);
        log.debug("### 登录成功 -> LoginTime={} | LoginIp={}", res.getLoginTime(), res.getLoginIp());
    }

    protected void clearLoginFailedCount(LoginSuccessEvent event) {
        LoginConfig loginConfig = event.getLoginConfig();
        if (!loginConfig.getLoginCaptcha().isNeedCaptcha()) {
            return;
        }
        AbstractUserLoginReq loginData = event.getLoginData();
        UserInfo userInfo = event.getUserInfo();
        Assert.notNull(loginData, "loginData不能为null");
        Assert.notNull(userInfo, "userInfo不能为null");
        ClearLoginFailedCountReq req = new ClearLoginFailedCountReq(event.getDomainId());
        req.setUid(userInfo.getUid());
        req.setLoginType(loginData.getLoginType().getId());
        ClearLoginFailedCountRes res = loginSupportClient.clearLoginFailedCount(req);
        log.debug("### 清除用户连续登录失败次数: {} | uid = [{}]", res.getFailedCount(), res.getUid());
    }

    protected void disableFirstToken(LoginSuccessEvent event) {
        LoginConfig loginConfig = event.getLoginConfig();
        if (loginConfig.getConcurrentLoginCount() <= 0) {
            return;
        }
        AbstractUserLoginReq loginData = event.getLoginData();
        UserInfo userInfo = event.getUserInfo();
        Assert.notNull(loginData, "loginData不能为null");
        Assert.notNull(userInfo, "userInfo不能为null");
        if (loginConfig.isAllowAfterLogin()) {
            // 获取当前用户并发登录数量
            GetConcurrentLoginCountReq req = new GetConcurrentLoginCountReq(event.getDomainId());
            req.setUid(userInfo.getUid());
            GetConcurrentLoginCountRes res = loginSupportClient.getConcurrentLoginCount(req);
            int realConcurrentLoginCount = res == null ? 0 : res.getConcurrentLoginCount();
            int disableCount = realConcurrentLoginCount - loginConfig.getConcurrentLoginCount();
            if (disableCount >= 1) {
                // 挤下最早登录的用户
                DisableFirstJwtTokenReq req2 = new DisableFirstJwtTokenReq(event.getDomainId());
                req2.setUid(userInfo.getUid());
                req2.setDisableReason("并发登录当数量超限，被挤下线");
                req2.setDisableCount(disableCount);
                DisableFirstJwtTokenRes res2 = loginSupportClient.disableFirstJwtToken(req2);
                log.debug("### 挤下最早登录的用户 -> uid={} | disableCount={}", res2.getUid(), res2.getDisableCount());
            }
        }
    }

    protected void writeBackScanCodeLogin(LoginSuccessEvent event, ScanCodeReq scanCodeReq) {
        WriteBackScanCodeLoginReq req = new WriteBackScanCodeLoginReq(event.getDomainId());
        req.setScanCode(scanCodeReq.getScanCode());
        req.setLoginTime(new Date());
        req.setScanCodeState(EnumConstant.ScanCodeLogin_ScanCodeState_3);
        req.setTokenId(Long.parseLong(event.getClaims().getId()));
        ScanCodeLogin res = loginSupportClient.writeBackScanCodeLogin(req);
        if (res != null) {
            log.debug("### 登陆成功回写扫码登录状态成功 | scanCode={} | scanCodeState={}", res.getScanCode(), res.getScanCodeState());
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
