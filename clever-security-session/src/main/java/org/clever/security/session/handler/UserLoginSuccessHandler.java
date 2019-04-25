package org.clever.security.session.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.security.dto.response.LoginRes;
import org.clever.security.dto.response.UserRes;
import org.clever.security.session.AttributeKeyConstant;
import org.clever.security.session.config.SecurityConfig;
import org.clever.security.session.utils.AuthenticationUtils;
import org.clever.security.session.utils.HttpRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
    @Autowired
    private SecurityConfig securityConfig;
    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        requestCache.removeRequest(request, response);
        clearAuthenticationAttributes(request);
        // 登录成功 - 是否需要跳转
        SecurityConfig.Login login = securityConfig.getLogin();
        if (login.getLoginSuccessNeedRedirect() != null) {
            if (login.getLoginSuccessNeedRedirect()) {
                // 跳转
                sendRedirect(request, response, getRedirectUrl(savedRequest, login.getLoginSuccessRedirectPage()));
            } else {
                // 不跳转
                sendJsonData(response, authentication);
            }
            return;
        }
        // 根据当前请求类型判断是否需要跳转页面
        if (HttpRequestUtils.isJsonResponseByLogin(request)) {
            sendJsonData(response, authentication);
            return;
        }
        // 根据savedRequest判断是否需要跳转 (之前访问的Url是一个页面才跳转)
        if (savedRequest != null && isJsonResponseBySavedRequest(savedRequest)) {
            sendJsonData(response, authentication);
            return;
        }
        sendRedirect(request, response, getRedirectUrl(savedRequest, login.getLoginSuccessRedirectPage()));
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
    private void sendJsonData(HttpServletResponse response, Authentication authentication) throws IOException {
        UserRes userRes = AuthenticationUtils.getUserRes(authentication);
        String json = JacksonMapper.nonEmptyMapper().toJson(new LoginRes(true, "登录成功", userRes));
        log.info("### 登录成功不需要跳转 -> [{}]", json);
        if (!response.isCommitted()) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(json);
        }
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

    /**
     * 清除Session中的异常信息
     */
    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        session.removeAttribute(AttributeKeyConstant.Login_Captcha_Session_Key);
        session.removeAttribute(AttributeKeyConstant.Login_Fail_Count_Session_Key);
    }
}
