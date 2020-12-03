package org.clever.security.embed.authentication;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.embed.authentication.login.*;
import org.clever.security.embed.collect.LoginDataCollect;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.LoginConfig;
import org.clever.security.embed.event.LoginFailureEvent;
import org.clever.security.embed.event.LoginSuccessEvent;
import org.clever.security.embed.exception.LoginException;
import org.clever.security.embed.exception.LoginInnerException;
import org.clever.security.embed.handler.LoginFailureHandler;
import org.clever.security.embed.handler.LoginSuccessHandler;
import org.clever.security.embed.utils.HttpServletResponseUtils;
import org.clever.security.embed.utils.JwtTokenUtils;
import org.clever.security.embed.utils.ListSortUtils;
import org.clever.security.model.UserInfo;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.clever.security.model.login.LoginRes;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 登录拦截器
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:09 <br/>
 */
@Slf4j
public class LoginFilter extends GenericFilterBean {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    /**
     * 收集登录数据
     */
    private final List<LoginDataCollect> loginDataCollectList;
    /**
     * 加载用户前后校验登录数据(字段格式、验证码等等)
     */
    private final List<VerifyLoginData> verifyLoginDataList;
    /**
     * 加载用户信息
     */
    private final List<LoadUser> loadUserList;
    /**
     * 加载用户之后校验登录数据(密码、验证码等)
     */
    private final List<VerifyUserInfo> verifyUserInfoList;
    /**
     * 创建JWT-Token时加入扩展数据
     */
    private final List<AddJwtTokenExtData> addJwtTokenExtDataList;
    /**
     * 登录成功处理
     */
    private final List<LoginSuccessHandler> loginSuccessHandlerList;
    /**
     * 登录失败处理
     */
    private final List<LoginFailureHandler> loginFailureHandlerList;

    public LoginFilter(
            SecurityConfig securityConfig,
            List<LoginDataCollect> loginDataCollectList,
            List<VerifyLoginData> verifyLoginDataList,
            List<LoadUser> loadUserList,
            List<VerifyUserInfo> verifyUserInfoList,
            List<AddJwtTokenExtData> addJwtTokenExtDataList,
            List<LoginSuccessHandler> loginSuccessHandlerList,
            List<LoginFailureHandler> loginFailureHandlerList) {
        Assert.notNull(securityConfig, "系统授权配置对象(SecurityConfig)不能为null");
        Assert.notEmpty(loginDataCollectList, "登录数据收集器(ILoginDataCollect)不存在");
        Assert.notEmpty(verifyLoginDataList, "用户登录验证器(IVerifyLoginData)不存在");
        Assert.notEmpty(loadUserList, "用户信息加载器(ILoadUser)不存在");
        Assert.notEmpty(verifyUserInfoList, "用户登录验证器(IVerifyUserInfo)不存在");
        this.securityConfig = securityConfig;
        this.loginDataCollectList = ListSortUtils.sort(loginDataCollectList);
        this.verifyLoginDataList = ListSortUtils.sort(verifyLoginDataList);
        this.loadUserList = ListSortUtils.sort(loadUserList);
        this.verifyUserInfoList = ListSortUtils.sort(verifyUserInfoList);
        this.addJwtTokenExtDataList = ListSortUtils.sort(addJwtTokenExtDataList);
        this.loginSuccessHandlerList = ListSortUtils.sort(loginSuccessHandlerList);
        this.loginFailureHandlerList = ListSortUtils.sort(loginFailureHandlerList);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            log.warn("[clever-security]仅支持HTTP服务器");
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (!isLoginRequest(httpRequest)) {
            // 不是登录请求
            chain.doFilter(request, response);
            return;
        }
        log.debug("### 开始执行登录逻辑 ---------------------------------------------------------------------->");
        // 执行登录逻辑
        LoginContext context = new LoginContext(httpRequest, httpResponse);
        try {
            login(context);
            // 登录成功 - 返回数据给客户端 TODO -- 登录成功服务器行为策略
            HttpServletResponseUtils.sendJson(httpResponse, LoginRes.loginSuccess(context.getUserInfo(), context.getJwtToken()));
        } catch (LoginException e) {
            // 登录失败 TODO -- 登录失败服务器行为策略
            HttpServletResponseUtils.sendJson(httpResponse, LoginRes.loginFailure(e.getMessage()));
        } catch (Throwable e) {
            // 登录异常
            HttpServletResponseUtils.sendJson(httpRequest, httpResponse, HttpStatus.INTERNAL_SERVER_ERROR, e);
        } finally {
            log.debug("### 登录逻辑执行完成 <----------------------------------------------------------------------");
        }
    }

    /**
     * 登录流程
     */
    protected void login(LoginContext context) throws Exception {
        // 收集登录数据
        LoginDataCollect loginDataCollect = null;
        for (LoginDataCollect collect : loginDataCollectList) {
            if (collect.isSupported(securityConfig, context.getRequest())) {
                loginDataCollect = collect;
            }
            if (loginDataCollect != null) {
                break;
            }
        }
        if (loginDataCollect == null) {
            context.setLoginException(new LoginInnerException("不支持的登录请求(无法获取登录数据)"));
            throw context.getLoginException();
        }
        AbstractUserLoginReq loginReq = loginDataCollect.collectLoginData(securityConfig, context.getRequest());
        context.setLoginData(loginReq);
        log.debug("### 收集登录数据 -> {}", loginReq);
        // 加载用户之前校验登录数据
        for (VerifyLoginData verifyLoginData : verifyLoginDataList) {
            if (!verifyLoginData.isSupported(securityConfig, context.getRequest(), loginReq)) {
                continue;
            }
            try {
                verifyLoginData.verify(securityConfig, context.getRequest(), loginReq);
            } catch (LoginException e) {
                context.setLoginException(e);
                break;
            }
        }
        // 登录失败
        if (context.isLoginFailure()) {
            log.debug("### 加载用户之前校验登录数据失败(登录失败) -> {}", loginReq, context.getLoginException());
            loginFailureHandler(context);
            throw context.getLoginException();
        } else {
            log.debug("### 加载用户之前校验登录数据成功 -> {}", loginReq);
        }
        // 加载用户信息
        LoadUser loadUser = null;
        for (LoadUser load : loadUserList) {
            if (load.isSupported(securityConfig, context.getRequest(), loginReq)) {
                loadUser = load;
                break;
            }
        }
        if (loadUser == null) {
            context.setLoginException(new LoginInnerException("用户信息不存在(无法加载用户信息)"));
            throw context.getLoginException();
        }
        UserInfo userInfo = loadUser.loadUserInfo(securityConfig, context.getRequest(), loginReq);
        context.setUserInfo(userInfo);
        log.debug("### 加载用户信息 -> {}", userInfo);
        // 加载用户之后校验登录数据
        for (VerifyUserInfo verifyUserInfo : verifyUserInfoList) {
            if (!verifyUserInfo.isSupported(securityConfig, context.getRequest(), loginReq, userInfo)) {
                continue;
            }
            try {
                verifyUserInfo.verify(securityConfig, context.getRequest(), loginReq, userInfo);
            } catch (LoginException e) {
                context.setLoginException(e);
                break;
            }
        }
        if (context.isLoginFailure()) {
            // 登录失败
            log.debug("### 加载用户之后校验登录数据失败(登录失败) -> {}", userInfo, context.getLoginException());
            loginFailureHandler(context);
            throw context.getLoginException();
        } else {
            // 登录成功
            log.debug("### 登录成功 -> {}", userInfo);
            final String jwtToken = JwtTokenUtils.createJwtToken(securityConfig.getTokenConfig(), userInfo, loginReq.isRememberMe(), addJwtTokenExtDataList);
            final String refreshToken = JwtTokenUtils.createRefreshToken(userInfo);
            log.debug("### 登录成功 | uid={} | jwt-token={} | refresh-token={}", userInfo.getUid(), jwtToken, refreshToken);
            context.setJwtToken(jwtToken);
            context.setRefreshToken(refreshToken);
            loginSuccessHandler(context);
        }
    }

    /**
     * 当前请求是否是登录请求
     */
    protected boolean isLoginRequest(HttpServletRequest httpRequest) {
        LoginConfig login = securityConfig.getLogin();
        if (login == null) {
            return false;
        }
        if (!Objects.equals(login.getLoginPath(), httpRequest.getServletPath())) {
            return false;
        }
        boolean postRequest = HttpMethod.POST.matches(httpRequest.getMethod());
        return !login.isPostOnly() || postRequest;
    }

    /**
     * 登录成功处理
     */
    protected void loginSuccessHandler(LoginContext context) throws Exception {
        if (loginSuccessHandlerList == null) {
            return;
        }
        LoginSuccessEvent loginSuccessEvent = new LoginSuccessEvent(
                context.getRequest(),
                context.getResponse(),
                context.getLoginData(),
                context.getUserInfo()
        );
        for (LoginSuccessHandler handler : loginSuccessHandlerList) {
            handler.onLoginSuccess(context.getRequest(), context.getResponse(), loginSuccessEvent);
        }
    }

    /**
     * 登录失败处理
     */
    protected void loginFailureHandler(LoginContext context) throws Exception {
        if (loginFailureHandlerList == null) {
            return;
        }
        LoginFailureEvent loginFailureEvent = new LoginFailureEvent(
                context.getRequest(),
                context.getResponse(),
                context.getLoginData(),
                context.getUserInfo(),
                context.getLoginException()
        );
        for (LoginFailureHandler handler : loginFailureHandlerList) {
            handler.onLoginFailure(context.getRequest(), context.getResponse(), loginFailureEvent);
        }
    }
}
