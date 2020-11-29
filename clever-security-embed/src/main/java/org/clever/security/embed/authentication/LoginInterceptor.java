package org.clever.security.embed.authentication;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.embed.collect.ILoginDataCollect;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.event.LoginFailureEvent;
import org.clever.security.embed.event.LoginSuccessEvent;
import org.clever.security.embed.exception.LoginException;
import org.clever.security.embed.handler.LoginFailureHandler;
import org.clever.security.embed.handler.LoginSuccessHandler;
import org.clever.security.embed.utils.ListSortUtils;
import org.clever.security.model.UserInfo;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.springframework.util.Assert;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户身份认证拦截器(登录拦截器)
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:09 <br/>
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    /**
     * 收集登录数据
     */
    private final List<ILoginDataCollect> loginDataCollectList;
    /**
     * 加载用户信息
     */
    private final List<ILoadUser> loadUserList;
    /**
     * 用户登录身份认证
     */
    private final List<IVerifyLoginData> verifyLoginDataList;
    /**
     * 登录成功处理
     */
    private final List<LoginSuccessHandler> loginSuccessHandlerList;
    /**
     * 登录失败处理
     */
    private final List<LoginFailureHandler> loginFailureHandlerList;

    public LoginInterceptor(
            SecurityConfig securityConfig,
            List<ILoginDataCollect> loginDataCollectList,
            List<ILoadUser> loadUserList,
            List<IVerifyLoginData> verifyLoginDataList,
            List<LoginSuccessHandler> loginSuccessHandlerList,
            List<LoginFailureHandler> loginFailureHandlerList) {
        Assert.notNull(securityConfig, "系统授权配置对象(SecurityConfig)不能为null");
        Assert.notEmpty(loginDataCollectList, "登录数据收集器(ILoginDataCollect)不存在");
        Assert.notEmpty(loadUserList, "用户信息加载器(ILoadUser)不存在");
        Assert.notEmpty(verifyLoginDataList, "用户登录身份认证器(IVerifyLoginData)不存在");
        this.securityConfig = securityConfig;
        this.loginDataCollectList = ListSortUtils.sort(loginDataCollectList);
        this.loadUserList = ListSortUtils.sort(loadUserList);
        this.verifyLoginDataList = ListSortUtils.sort(verifyLoginDataList);
        this.loginSuccessHandlerList = ListSortUtils.sort(loginSuccessHandlerList);
        this.loginFailureHandlerList = ListSortUtils.sort(loginFailureHandlerList);
    }

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nullable Object handler) {
        try {
            login(request, response);
        } catch (Exception e) {
            // TODO
        }
        return false;
    }

    protected void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Context

        // 收集登录数据
        ILoginDataCollect loginDataCollect = null;
        for (ILoginDataCollect collect : loginDataCollectList) {
            if (collect.isSupported(securityConfig, request)) {
                loginDataCollect = collect;
                break;
            }
        }
        if (loginDataCollect == null) {
            // TODO
            throw new RuntimeException("不支持的登录请求(登录数据错误)");
        }
        AbstractUserLoginReq loginReq = loginDataCollect.collectLoginData(securityConfig, request);
        // 加载用户信息
        ILoadUser loadUser = null;
        for (ILoadUser load : loadUserList) {
            if (load.isSupported(securityConfig, request, loginReq)) {
                loadUser = load;
                break;
            }
        }
        if (loadUser == null) {
            // TODO
            throw new RuntimeException("用户信息不存在");
        }
        UserInfo userInfo = loadUser.loadUserInfo(securityConfig, request, loginReq);
        // 身份认证
        IVerifyLoginData verifyLoginData = null;
        for (IVerifyLoginData verify : verifyLoginDataList) {
            if (verify.isSupported(securityConfig, request, loginReq, userInfo)) {
                verifyLoginData = verify;
                break;
            }
        }
        if (verifyLoginData == null) {
            // TODO
            throw new RuntimeException("身份认证失败");
        }
        LoginException loginException = null;
        try {
            verifyLoginData.authentication(securityConfig, request, loginReq, userInfo);
        } catch (LoginException e) {
            // TODO
            loginException = e;
        }
        // 登录成功
        if (loginException == null && loginSuccessHandlerList != null) {
            LoginSuccessEvent loginSuccessEvent = new LoginSuccessEvent();
            for (LoginSuccessHandler handler : loginSuccessHandlerList) {
                handler.onLoginSuccess(request, response, loginSuccessEvent);
            }
        }
        // 登录失败
        if (loginException != null && loginFailureHandlerList != null) {
            LoginFailureEvent loginFailureEvent = new LoginFailureEvent(loginException);
            for (LoginFailureHandler handler : loginFailureHandlerList) {
                handler.onLoginFailure(request, response, loginFailureEvent);
            }
        }
    }
}
