package org.clever.security.authentication.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.security.Constant;
import org.clever.security.LoginModel;
import org.clever.security.authentication.CollectLoginToken;
import org.clever.security.config.SecurityConfig;
import org.clever.security.config.model.LoginConfig;
import org.clever.security.dto.response.JwtLoginRes;
import org.clever.security.dto.response.LoginRes;
import org.clever.security.dto.response.UserRes;
import org.clever.security.exception.BadCaptchaException;
import org.clever.security.exception.BadLoginTypeException;
import org.clever.security.handler.UserLoginFailureHandler;
import org.clever.security.handler.UserLoginSuccessHandler;
import org.clever.security.model.CaptchaInfo;
import org.clever.security.repository.CaptchaInfoRepository;
import org.clever.security.repository.LoginFailCountRepository;
import org.clever.security.repository.RedisJwtRepository;
import org.clever.security.token.JwtAccessToken;
import org.clever.security.token.login.BaseLoginToken;
import org.clever.security.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 收集用户登录信息
 * 作者： lzw<br/>
 * 创建时间：2018-03-15 12:48 <br/>
 */
@Component
@Slf4j
public class UserLoginFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private UserLoginSuccessHandler userLoginSuccessHandler;
    @Autowired
    private UserLoginFailureHandler userLoginFailureHandler;
    @Autowired
    private RedisJwtRepository redisJwtRepository;
    @Autowired
    private CaptchaInfoRepository captchaInfoRepository;
    @Autowired
    private LoginFailCountRepository loginFailCountRepository;
    @Autowired
    private List<CollectLoginToken> collectLoginTokenList;
    private AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
    private boolean postOnly = true;
    /**
     * 登录是否需要验证码
     */
    private Boolean needCaptcha = true;
    /**
     * 登录失败多少次才需要验证码(小于等于0,总是需要验证码)
     */
    private Integer needCaptchaByLoginFailCount = 3;

    public UserLoginFilter(SecurityConfig securityConfig) {
        super(new AntPathRequestMatcher(securityConfig.getLogin().getLoginUrl()));
    }

    @Autowired
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @PostConstruct
    public void init() {
        log.info("### UserLoginFilter 初始化配置");
        this.setBeanName(this.toString());
        // 初始化配置
        LoginConfig login = securityConfig.getLogin();
        if (login != null) {
            if (login.getPostOnly() != null) {
                postOnly = login.getPostOnly();
            }
            if (login.getNeedCaptcha() != null) {
                needCaptcha = login.getNeedCaptcha();
            }
            if (login.getNeedCaptchaByLoginFailCount() != null) {
                needCaptchaByLoginFailCount = login.getNeedCaptchaByLoginFailCount();
            }
        }
        if (collectLoginTokenList == null || this.collectLoginTokenList.size() <= 0) {
            throw new RuntimeException("未注入收集用户登录登录信息组件");
        }
        this.setAuthenticationSuccessHandler(userLoginSuccessHandler);
        this.setAuthenticationFailureHandler(userLoginFailureHandler);
    }

    /**
     * 收集认证信息
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        if (postOnly && !request.getMethod().equalsIgnoreCase("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        // 需要防止用户重复登录
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authenticationTrustResolver.isRememberMe(authentication)) {
            // 已经登录成功了
            UserRes userRes = AuthenticationUtils.getUserRes(authentication);
            String json;
            if (LoginModel.jwt.equals(securityConfig.getLoginModel())) {
                // JWT
                JwtAccessToken jwtAccessToken = redisJwtRepository.getJwtToken(request);
                JwtLoginRes jwtLoginRes = new JwtLoginRes(true, "您已经登录成功了无须多次登录", userRes, jwtAccessToken.getToken(), jwtAccessToken.getRefreshToken());
                json = JacksonMapper.nonEmptyMapper().toJson(jwtLoginRes);
            } else {
                // Session
                json = JacksonMapper.nonEmptyMapper().toJson(new LoginRes(true, "您已经登录成功了无须多次登录", userRes));
            }
            if (!response.isCommitted()) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json;charset=utf-8");
                try {
                    response.getWriter().print(json);
                } catch (IOException e) {
                    throw new InternalAuthenticationServiceException("返回数据写入响应流失败", e);
                }
            }
            log.info("### 当前用户已登录 [{}]", authentication.toString());
            return null;
        }

        boolean isSubmitBody = securityConfig.getLogin().getJsonDataSubmit();
        BaseLoginToken loginToken = null;
        // 获取用户登录信息
        for (CollectLoginToken collectLoginToken : collectLoginTokenList) {
            if (collectLoginToken.supports(request, isSubmitBody)) {
                loginToken = collectLoginToken.attemptAuthentication(request, isSubmitBody);
                break;
            }
        }
        if (loginToken == null) {
            throw new BadLoginTypeException("不支持的登录请求");
        }
        // 设置用户 "details" 属性(设置请求IP和SessionID) -- 需要提前创建Session
        if (LoginModel.session.equals(securityConfig.getLoginModel())) {
            request.getSession();
        }
        loginToken.setDetails(authenticationDetailsSource.buildDetails(request));
        log.info("### 用户登录开始，构建UserLoginToken [{}]", loginToken.toString());
        request.setAttribute(Constant.Login_Username_Request_Key, loginToken.getName());

        //  读取验证码 - 验证
        if (needCaptcha) {
            long loginFailCount = 0;
            CaptchaInfo captchaInfo;
            if (LoginModel.jwt.equals(securityConfig.getLoginModel())) {
                // JWT
                loginFailCount = loginFailCountRepository.getLoginFailCount(loginToken.getName());
                if (loginFailCount > needCaptchaByLoginFailCount) {
                    captchaInfo = captchaInfoRepository.getCaptchaInfo(loginToken.getCaptcha(), loginToken.getCaptchaDigest());
                    verifyCaptchaInfo(captchaInfo, loginToken.getCaptcha());
                    captchaInfoRepository.deleteCaptchaInfo(loginToken.getCaptcha(), loginToken.getCaptchaDigest());
                }
            } else {
                // Session
                Object loginFailCountStr = request.getSession().getAttribute(Constant.Login_Fail_Count_Session_Key);
                if (loginFailCountStr != null) {
                    loginFailCount = NumberUtils.toInt(loginFailCountStr.toString(), 0);
                }
                if (loginFailCount > needCaptchaByLoginFailCount) {
                    captchaInfo = (CaptchaInfo) request.getSession().getAttribute(Constant.Login_Captcha_Session_Key);
                    verifyCaptchaInfo(captchaInfo, loginToken.getCaptcha());
                    request.getSession().removeAttribute(Constant.Login_Captcha_Session_Key);
                }
            }
            log.info("### 验证码校验通过");
        }
        // 验证登录
        return this.getAuthenticationManager().authenticate(authentication);
    }

    /**
     * 校验验证码
     */
    private void verifyCaptchaInfo(CaptchaInfo captchaInfo, String captcha) {
        if (captchaInfo == null) {
            throw new BadCaptchaException("验证码不存在");
        }
        if (captchaInfo.getEffectiveTime() > 0 && System.currentTimeMillis() - captchaInfo.getCreateTime() >= captchaInfo.getEffectiveTime()) {
            throw new BadCaptchaException("验证码已过期");
        }
        if (!captchaInfo.getCode().equalsIgnoreCase(captcha)) {
            throw new BadCaptchaException("验证码不匹配");
        }
    }
}
