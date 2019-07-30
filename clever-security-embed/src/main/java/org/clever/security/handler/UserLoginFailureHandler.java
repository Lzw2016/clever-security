package org.clever.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.clever.security.Constant;
import org.clever.security.LoginModel;
import org.clever.security.client.UserClient;
import org.clever.security.config.SecurityConfig;
import org.clever.security.config.model.LoginConfig;
import org.clever.security.dto.response.LoginRes;
import org.clever.security.exception.BadCaptchaException;
import org.clever.security.exception.BadLoginTypeException;
import org.clever.security.exception.CanNotLoginSysException;
import org.clever.security.exception.ConcurrentLoginException;
import org.clever.security.repository.LoginFailCountRepository;
import org.clever.security.utils.HttpResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义登录失败处理类
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-03-17 23:04 <br/>
 */
@Component
@Slf4j
public class UserLoginFailureHandler implements AuthenticationFailureHandler {

    private final String defaultRedirectUrl = "/index.html";
    private final Map<String, String> failureMessageMap = new HashMap<>();
    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private LoginFailCountRepository loginFailCountRepository;
    @Autowired
    private UserClient userClient;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private boolean hideUserNotFoundExceptions = true;

    public UserLoginFailureHandler(SecurityConfig securityConfig) {
        failureMessageMap.put(UsernameNotFoundException.class.getName(), "用户不存在");
        failureMessageMap.put(BadCredentialsException.class.getName(), "用户名或密码错误");
        failureMessageMap.put(AccountStatusException.class.getName(), "账户状态异常");
        failureMessageMap.put(AccountExpiredException.class.getName(), "账户已过期");
        failureMessageMap.put(LockedException.class.getName(), "账户被锁定");
        failureMessageMap.put(DisabledException.class.getName(), "账户被禁用");
        failureMessageMap.put(CredentialsExpiredException.class.getName(), "密码已过期");
        failureMessageMap.put(BadCaptchaException.class.getName(), "验证码错误");
        failureMessageMap.put(BadLoginTypeException.class.getName(), "不支持的登录类型");
        failureMessageMap.put(CanNotLoginSysException.class.getName(), "您无权登录当前系统，请联系管理员授权");
        failureMessageMap.put(ConcurrentLoginException.class.getName(), "并发登录数量超限");
        if (securityConfig.getHideUserNotFoundExceptions() != null) {
            hideUserNotFoundExceptions = securityConfig.getHideUserNotFoundExceptions();
        }
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        Integer loginFailCount = null;
        LoginConfig login = securityConfig.getLogin();
        if (login.getNeedCaptcha()) {
            if (LoginModel.jwt.equals(securityConfig.getLoginModel())) {
                // JwtToken 记录登录失败次数
                Object object = request.getAttribute(Constant.Login_Username_Request_Key);
                if (object != null && StringUtils.isNotBlank(object.toString()) && userClient.canLogin(object.toString(), securityConfig.getSysName())) {
                    loginFailCount = Long.valueOf(loginFailCountRepository.incrementLoginFailCount(object.toString())).intValue();
                }
            } else {
                // Session 记录登录次数
                Object loginFailCountStr = request.getSession().getAttribute(Constant.Login_Fail_Count_Session_Key);
                loginFailCount = 1;
                if (loginFailCountStr != null) {
                    loginFailCount = NumberUtils.toInt(loginFailCountStr.toString(), 0) + 1;
                }
                request.getSession().setAttribute(Constant.Login_Fail_Count_Session_Key, loginFailCount);
            }
        }
        // 登录失败 - 是否需要跳转
        if (login.getLoginFailureNeedRedirect() != null && login.getLoginFailureNeedRedirect()) {
            // 跳转
            HttpSession session = request.getSession(false);
            if (session != null) {
                request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
            }
            sendRedirect(request, response, login.getLoginFailureRedirectPage());
            return;
        }
        // 请求转发
        if (login.getLoginFailureNeedForward() != null && login.getLoginFailureNeedForward()) {
            request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
            sendForward(request, response, login.getLoginFailureRedirectPage());
            return;
        }
        // 不需要跳转
        sendJsonData(response, exception, login, loginFailCount);
    }

    /**
     * 直接返回Json数据
     */
    private void sendJsonData(HttpServletResponse response, AuthenticationException exception, LoginConfig login, Integer loginFailCount) {
        String message = failureMessageMap.get(exception.getClass().getName());
        if (hideUserNotFoundExceptions && exception instanceof UsernameNotFoundException) {
            message = failureMessageMap.get(BadCredentialsException.class.getName());
        }
        if (StringUtils.isBlank(message)) {
            message = "登录失败";
        }
        LoginRes loginRes = new LoginRes(false, message);
        if (loginFailCount != null) {
            if (login.getNeedCaptcha() && login.getNeedCaptchaByLoginFailCount() <= loginFailCount) {
                // 下次登录需要验证码
                loginRes.setNeedCaptcha(true);
            }
        }
        log.info("### 登录失败不需要跳转 -> [{}]", loginRes);
        HttpResponseUtils.sendJsonBy401(response, loginRes);
    }

    /**
     * 页面跳转 (重定向)
     */
    private void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        if (StringUtils.isBlank(url)) {
            url = defaultRedirectUrl;
        }
        log.info("### 登录失败跳转Url(重定向) -> {}", url);
        if (!response.isCommitted()) {
            redirectStrategy.sendRedirect(request, response, url);
        }
    }

    /**
     * 页面跳转 (请求转发)
     */
    private void sendForward(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException {
        if (StringUtils.isBlank(url)) {
            url = defaultRedirectUrl;
        }
        log.info("### 登录失败跳转Url(请求转发) -> {}", url);
        request.getRequestDispatcher(url).forward(request, response);
    }
}
