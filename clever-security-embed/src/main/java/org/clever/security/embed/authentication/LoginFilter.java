package org.clever.security.embed.authentication;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.CookieUtils;
import org.clever.common.utils.DateTimeUtils;
import org.clever.common.utils.tuples.TupleTow;
import org.clever.security.embed.authentication.login.*;
import org.clever.security.embed.collect.LoginDataCollect;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.LoginConfig;
import org.clever.security.embed.config.internal.TokenConfig;
import org.clever.security.embed.context.SecurityContextHolder;
import org.clever.security.embed.context.SecurityContextRepository;
import org.clever.security.embed.event.LoginFailureEvent;
import org.clever.security.embed.event.LoginSuccessEvent;
import org.clever.security.embed.exception.CollectLoginDataException;
import org.clever.security.embed.exception.LoginException;
import org.clever.security.embed.exception.LoginInnerException;
import org.clever.security.embed.exception.RepeatLoginException;
import org.clever.security.embed.handler.LoginFailureHandler;
import org.clever.security.embed.handler.LoginSuccessHandler;
import org.clever.security.embed.utils.HttpServletResponseUtils;
import org.clever.security.embed.utils.JwtTokenUtils;
import org.clever.security.embed.utils.ListSortUtils;
import org.clever.security.embed.utils.PathFilterUtils;
import org.clever.security.model.UserInfo;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.clever.security.model.login.LoginRes;
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
import java.util.Date;
import java.util.List;

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
    /**
     * 加载安全上下文(用户信息)
     */
    private final SecurityContextRepository securityContextRepository;

    public LoginFilter(
            SecurityConfig securityConfig,
            List<LoginDataCollect> loginDataCollectList,
            List<VerifyLoginData> verifyLoginDataList,
            List<LoadUser> loadUserList,
            List<VerifyUserInfo> verifyUserInfoList,
            List<AddJwtTokenExtData> addJwtTokenExtDataList,
            List<LoginSuccessHandler> loginSuccessHandlerList,
            List<LoginFailureHandler> loginFailureHandlerList,
            SecurityContextRepository securityContextRepository) {
        Assert.notNull(securityConfig, "权限系统配置对象(SecurityConfig)不能为null");
        Assert.notEmpty(loginDataCollectList, "登录数据收集器(ILoginDataCollect)不存在");
        Assert.notEmpty(verifyLoginDataList, "用户登录验证器(IVerifyLoginData)不存在");
        Assert.notEmpty(loadUserList, "用户信息加载器(ILoadUser)不存在");
        Assert.notEmpty(verifyUserInfoList, "用户登录验证器(IVerifyUserInfo)不存在");
        Assert.notNull(securityContextRepository, "安全上下文存取器(SecurityContextRepository)不能为null");
        this.securityConfig = securityConfig;
        this.loginDataCollectList = ListSortUtils.sort(loginDataCollectList);
        this.verifyLoginDataList = ListSortUtils.sort(verifyLoginDataList);
        this.loadUserList = ListSortUtils.sort(loadUserList);
        this.verifyUserInfoList = ListSortUtils.sort(verifyUserInfoList);
        this.addJwtTokenExtDataList = ListSortUtils.sort(addJwtTokenExtDataList);
        this.loginSuccessHandlerList = ListSortUtils.sort(loginSuccessHandlerList);
        this.loginFailureHandlerList = ListSortUtils.sort(loginFailureHandlerList);
        this.securityContextRepository = securityContextRepository;
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
        if (!PathFilterUtils.isLoginRequest(httpRequest, securityConfig)) {
            // 不是登录请求
            chain.doFilter(request, response);
            return;
        }
        log.debug("### 开始执行登录逻辑 ---------------------------------------------------------------------->");
        // 执行登录逻辑
        LoginContext context = new LoginContext(httpRequest, httpResponse);
        try {
            login(context);
            // 登录成功 - 返回数据给客户端
            onLoginSuccessResponse(context);
        } catch (LoginException e) {
            // 登录失败
            log.debug("### 登录失败", e);
            // context.setLoginException(e);
            try {
                onLoginFailureResponse(context);
            } catch (Exception innerException) {
                log.error("登录异常", innerException);
                HttpServletResponseUtils.sendJson(httpRequest, httpResponse, HttpStatus.INTERNAL_SERVER_ERROR, innerException);
            }
        } catch (Throwable e) {
            // 登录异常
            log.error("登录异常", e);
            HttpServletResponseUtils.sendJson(httpRequest, httpResponse, HttpStatus.INTERNAL_SERVER_ERROR, e);
        } finally {
            log.debug("### 登录逻辑执行完成 <----------------------------------------------------------------------");
        }
    }

    /**
     * 登录流程
     */
    protected void login(LoginContext context) throws Exception {
        // 判断用户是否重复登录
        if (!securityConfig.getLogin().isAllowRepeatLogin() && SecurityContextHolder.containsContext(context.getRequest())) {
            context.setLoginException(new RepeatLoginException("不支持用户重复登录,必须先退出当前账户"));
            throw context.getLoginException();
        }
        // 收集登录数据
        AbstractUserLoginReq loginReq = null;
        for (LoginDataCollect collect : loginDataCollectList) {
            if (!collect.isSupported(securityConfig, context.getRequest())) {
                continue;
            }
            try {
                loginReq = collect.collectLoginData(securityConfig, context.getRequest());
            } catch (Exception e) {
                context.setLoginException(new CollectLoginDataException("读取登录数据失败", e));
                throw context.getLoginException();
            }
            if (loginReq != null) {
                break;
            }
        }
        log.debug("### 收集登录数据 -> {}", loginReq);
        if (loginReq == null) {
            context.setLoginException(new LoginInnerException("不支持的登录请求(无法获取登录数据)"));
            throw context.getLoginException();
        }
        context.setLoginData(loginReq);
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
            TokenConfig tokenConfig = securityConfig.getTokenConfig();
            final TupleTow<String, Claims> tokenInfo = JwtTokenUtils.createJwtToken(tokenConfig, userInfo, loginReq.getRememberMe(), addJwtTokenExtDataList);
            final String refreshToken = JwtTokenUtils.createRefreshToken(userInfo);
            log.debug("### 登录成功 | uid={} | jwt-token={} | refresh-token={}", userInfo.getUid(), tokenInfo.getValue1(), refreshToken);
            context.setJwtToken(tokenInfo.getValue1());
            context.setRefreshToken(refreshToken);
            // 保存安全上下文(用户信息)
            securityContextRepository.saveContext(context, securityConfig, context.getRequest(), context.getResponse());
            // 将JWT-Token写入客户端
            if (tokenConfig.isUseCookie()) {
                int maxAge = DateTimeUtils.pastSeconds(new Date(), tokenInfo.getValue2().getExpiration()) + (60 * 3);
                CookieUtils.setCookie(context.getResponse(), "/", tokenConfig.getJwtTokenName(), tokenInfo.getValue1(), maxAge);
            } else {
                context.getResponse().addHeader(tokenConfig.getJwtTokenName(), tokenInfo.getValue1());
            }
            // 登录成功处理
            loginSuccessHandler(context);
        }
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

    /**
     * 当登录成功时响应处理
     */
    protected void onLoginSuccessResponse(LoginContext context) throws IOException {
        if (context.getResponse().isCommitted()) {
            return;
        }
        LoginConfig login = securityConfig.getLogin();
        if (login != null && login.isLoginSuccessNeedRedirect()) {
            // 需要重定向
            HttpServletResponseUtils.redirect(context.getResponse(), login.getLoginSuccessRedirectPage());
        } else {
            // 直接返回
            LoginRes loginRes = LoginRes.loginSuccess(context.getUserInfo(), context.getJwtToken(), context.getRefreshToken());
            HttpServletResponseUtils.sendJson(context.getResponse(), loginRes, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 当登录失败时响应处理
     */
    protected void onLoginFailureResponse(LoginContext context) throws IOException {
        if (context.getResponse().isCommitted()) {
            return;
        }
        LoginConfig login = securityConfig.getLogin();
        if (login.isLoginFailureNeedRedirect()) {
            // 需要重定向
            HttpServletResponseUtils.redirect(context.getResponse(), login.getLoginFailureRedirectPage());
        } else {
            // 直接返回
            LoginRes loginRes = LoginRes.loginFailure(context.getLoginException().getMessage());
            HttpStatus httpStatus = (context.getLoginException() instanceof RepeatLoginException) ? HttpStatus.BAD_REQUEST : HttpStatus.UNAUTHORIZED;
            HttpServletResponseUtils.sendJson(context.getResponse(), loginRes, httpStatus);
        }
    }
}
