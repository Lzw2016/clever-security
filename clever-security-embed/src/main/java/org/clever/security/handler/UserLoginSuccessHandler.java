package org.clever.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.utils.CookieUtils;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.security.Constant;
import org.clever.security.LoginModel;
import org.clever.security.client.UserLoginLogClient;
import org.clever.security.config.SecurityConfig;
import org.clever.security.config.model.LoginConfig;
import org.clever.security.dto.request.UserLoginLogAddReq;
import org.clever.security.dto.response.JwtLoginRes;
import org.clever.security.dto.response.LoginRes;
import org.clever.security.dto.response.UserRes;
import org.clever.security.entity.EnumConstant;
import org.clever.security.repository.LoginFailCountRepository;
import org.clever.security.repository.RedisJwtRepository;
import org.clever.security.token.JwtAccessToken;
import org.clever.security.utils.AuthenticationUtils;
import org.clever.security.utils.HttpRequestUtils;
import org.clever.security.utils.HttpResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 自定义登录成功处理类
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-03-16 11:06 <br/>
 */
@Component
@Slf4j
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final String defaultRedirectUrl = "/home.html";
    private SecurityConfig securityConfig;
    private RequestCache requestCache;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Autowired
    private RedisJwtRepository redisJwtRepository;
    @Autowired
    private LoginFailCountRepository loginFailCountRepository;
    @Autowired
    private UserLoginLogClient userLoginLogClient;

    public UserLoginSuccessHandler(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
        if (LoginModel.jwt.equals(securityConfig.getLoginModel())) {
            requestCache = new NullRequestCache();
        } else {
            requestCache = new HttpSessionRequestCache();
        }
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        SavedRequest savedRequest = null;
        if (LoginModel.session.equals(securityConfig.getLoginModel())) {
            savedRequest = requestCache.getRequest(request, response);
        }
        requestCache.removeRequest(request, response);
        clearAuthenticationAttributes(request, authentication.getName());
        if (LoginModel.jwt.equals(securityConfig.getLoginModel())) {
            // 登录成功保存 JwtAccessToken JwtRefreshToken
            JwtAccessToken jwtAccessToken = redisJwtRepository.saveJwtToken(AuthenticationUtils.getSecurityContextToken(authentication));
            redisJwtRepository.saveSecurityContext(new SecurityContextImpl(authentication));
            log.info("### 已保存 JWT Token 和 SecurityContext");
            // 写入登录成功日志
            addLoginLog(authentication, jwtAccessToken);
            // 登录成功 - 返回JSon数据
            sendJsonData(response, authentication, jwtAccessToken);
            return;
        }
        LoginConfig login = securityConfig.getLogin();
        if (login.getLoginSuccessNeedRedirect() != null) {
            if (login.getLoginSuccessNeedRedirect()) {
                // 跳转
                sendRedirect(request, response, getRedirectUrl(savedRequest, login.getLoginSuccessRedirectPage()));
            } else {
                // 不跳转
                sendJsonData(response, authentication, null);
            }
            return;
        }
        // 根据当前请求类型判断是否需要跳转页面
        if (HttpRequestUtils.isJsonResponseByLogin(request)) {
            sendJsonData(response, authentication, null);
            return;
        }
        // 根据savedRequest判断是否需要跳转 (之前访问的Url是一个页面才跳转)
        if (savedRequest != null && isJsonResponseBySavedRequest(savedRequest)) {
            sendJsonData(response, authentication, null);
            return;
        }
        sendRedirect(request, response, getRedirectUrl(savedRequest, login.getLoginSuccessRedirectPage()));
    }

    /**
     * 清除登录异常信息
     */
    private void clearAuthenticationAttributes(HttpServletRequest request, String username) {
        if (LoginModel.jwt.equals(securityConfig.getLoginModel())) {
            loginFailCountRepository.deleteLoginFailCount(username);
        }
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        session.removeAttribute(Constant.Login_Captcha_Session_Key);
        session.removeAttribute(Constant.Login_Fail_Count_Session_Key);
    }

    /**
     * 写入登录成功日志(JwtToken日志)
     */
    @SuppressWarnings("Duplicates")
    private void addLoginLog(Authentication authentication, JwtAccessToken jwtAccessToken) {
        String JwtTokenId = jwtAccessToken.getClaims().getId();
        String loginIp = null;
        if (authentication.getDetails() instanceof WebAuthenticationDetails) {
            WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) authentication.getDetails();
            loginIp = webAuthenticationDetails.getRemoteAddress();
        }
        UserLoginLogAddReq userLoginLog = new UserLoginLogAddReq();
        userLoginLog.setSysName(securityConfig.getSysName());
        userLoginLog.setUsername(authentication.getName());
        userLoginLog.setLoginTime(new Date());
        userLoginLog.setLoginIp(StringUtils.trimToEmpty(loginIp));
        userLoginLog.setAuthenticationInfo(JacksonMapper.getInstance().toJson(authentication));
        userLoginLog.setLoginModel(EnumConstant.ServiceSys_LoginModel_1);
        userLoginLog.setSessionId(StringUtils.trimToEmpty(JwtTokenId));
        userLoginLog.setLoginState(EnumConstant.UserLoginLog_LoginState_1);
        try {
            userLoginLogClient.addUserLoginLog(userLoginLog);
            log.info("### 写入登录成功日志 [{}]", authentication.getName());
        } catch (Exception e) {
            log.error("写入登录成功日志失败 [{}]", authentication.getName(), e);
        }
    }

    /**
     * 根据savedRequest判断是否需要跳转
     */
    private boolean isJsonResponseBySavedRequest(SavedRequest savedRequest) {
        if (savedRequest == null) {
            return true;
        }
        boolean jsonFlag = false;
        boolean ajaxFlag = false;
        List<String> acceptHeaders = savedRequest.getHeaderValues("Accept");
        if (acceptHeaders != null) {
            for (String accept : acceptHeaders) {
                if ("application/json".equalsIgnoreCase(accept)) {
                    jsonFlag = true;
                    break;
                }
            }
        }
        List<String> ajaxHeaders = savedRequest.getHeaderValues("X-Requested-With");
        if (ajaxHeaders != null) {
            for (String ajax : ajaxHeaders) {
                if ("XMLHttpRequest".equalsIgnoreCase(ajax)) {
                    ajaxFlag = true;
                    break;
                }
            }
        }
        return jsonFlag || ajaxFlag;
    }

    /**
     * 获取需要跳转的Url
     */
    private String getRedirectUrl(SavedRequest savedRequest, String defaultUrl) {
        String url = null;
        if (savedRequest != null && !isJsonResponseBySavedRequest(savedRequest)) {
            url = savedRequest.getRedirectUrl();
        }
        if (StringUtils.isBlank(url)) {
            url = defaultUrl;
        }
        if (StringUtils.isBlank(url)) {
            url = defaultRedirectUrl;
        }
        return url;
    }

    /**
     * 直接返回Json数据
     */
    private void sendJsonData(HttpServletResponse response, Authentication authentication, JwtAccessToken jwtAccessToken) {
        Object resData;
        if (jwtAccessToken == null) {
            UserRes userRes = AuthenticationUtils.getUserRes(authentication);
            resData = new LoginRes(true, "登录成功", userRes);
        } else {
            if (securityConfig.getTokenConfig().isUseCookie()) {
                CookieUtils.setRooPathCookie(response, securityConfig.getTokenConfig().getJwtTokenKey(), jwtAccessToken.getToken());
            }
            response.setHeader(securityConfig.getTokenConfig().getJwtTokenKey(), jwtAccessToken.getToken());
            UserRes userRes = AuthenticationUtils.getUserRes(authentication);
            resData = new JwtLoginRes(true, "登录成功", userRes, jwtAccessToken.getToken(), jwtAccessToken.getRefreshToken());
        }
        HttpResponseUtils.sendJsonBy200(response, resData);
        log.info("### 登录成功不需要跳转 -> [{}]", resData);
    }

    /**
     * 页面跳转 (重定向)
     */
    private void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        if (StringUtils.isBlank(url)) {
            url = defaultRedirectUrl;
        }
        log.info("### 登录成功跳转Url(重定向) -> {}", url);
        if (!response.isCommitted()) {
            redirectStrategy.sendRedirect(request, response, url);
        }
    }
}
