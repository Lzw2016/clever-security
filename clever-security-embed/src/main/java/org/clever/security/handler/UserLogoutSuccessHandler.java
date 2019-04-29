package org.clever.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.security.config.SecurityConfig;
import org.clever.security.config.model.LogoutConfig;
import org.clever.security.dto.response.LogoutRes;
import org.clever.security.dto.response.UserRes;
import org.clever.security.utils.AuthenticationUtils;
import org.clever.security.utils.HttpRequestUtils;
import org.clever.security.utils.HttpResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-21 10:21 <br/>
 */
@Component
@Slf4j
public class UserLogoutSuccessHandler implements LogoutSuccessHandler {

    @SuppressWarnings("FieldCanBeLocal")
    private final String defaultRedirectUrl = "/index.html";
    @Autowired
    private SecurityConfig securityConfig;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication == null) {
            log.info("### 登出无效(还未登录)");
        } else {
            log.info("### 用户登出成功 [username={}]", authentication.getPrincipal().toString());
        }
        LogoutConfig logout = securityConfig.getLogout();
        if (logout.getLogoutSuccessNeedRedirect() != null && logout.getLogoutSuccessNeedRedirect()) {
            sendRedirect(request, response, logout.getLogoutSuccessRedirectPage());
            return;
        }
        // 判断是否需要跳转到页面
        if (HttpRequestUtils.isJsonResponse(request)) {
            sendJsonData(response, authentication);
            return;
        }
        sendRedirect(request, response, logout.getLogoutSuccessRedirectPage());
    }

    /**
     * 页面跳转 (重定向)
     */
    private void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        if (StringUtils.isBlank(url)) {
            url = defaultRedirectUrl;
        }
        log.info("### 登出成功跳转Url(重定向) -> {}", url);
        if (!response.isCommitted()) {
            redirectStrategy.sendRedirect(request, response, url);
        }
    }

    /**
     * 直接返回Json数据
     */
    private void sendJsonData(HttpServletResponse response, Authentication authentication) {
        UserRes userRes = AuthenticationUtils.getUserRes(authentication);
        LogoutRes logoutRes = new LogoutRes(true, "登出成功", userRes);
        HttpResponseUtils.sendJsonBy200(response, logoutRes);
        log.info("### 登出成功不需要跳转 -> [{}]", logoutRes);
    }
}
