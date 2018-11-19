package org.clever.security.jwt.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.security.dto.response.JwtLoginRes;
import org.clever.security.dto.response.UserRes;
import org.clever.security.jwt.AttributeKeyConstant;
import org.clever.security.jwt.config.SecurityConfig;
import org.clever.security.jwt.exception.BadCaptchaException;
import org.clever.security.jwt.handler.UserLoginFailureHandler;
import org.clever.security.jwt.handler.UserLoginSuccessHandler;
import org.clever.security.jwt.model.CaptchaInfo;
import org.clever.security.jwt.model.JwtToken;
import org.clever.security.jwt.model.UserLoginToken;
import org.clever.security.jwt.service.GenerateKeyService;
import org.clever.security.jwt.service.JWTTokenService;
import org.clever.security.jwt.utils.AuthenticationUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-15 12:48 <br/>
 */
@SuppressWarnings({"Duplicates", "FieldCanBeLocal"})
@Component
@Slf4j
public class UserLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final String LOGIN_TYPE_KEY = "loginType";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String CAPTCHA_KEY = "captcha";

    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private UserLoginSuccessHandler userLoginSuccessHandler;
    @Autowired
    private UserLoginFailureHandler userLoginFailureHandler;
    @Autowired
    private JWTTokenService jwtTokenService;
    @Autowired
    private GenerateKeyService generateKeyService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
    private String loginTypeParameter = LOGIN_TYPE_KEY;
    private String usernameParameter = USERNAME_KEY;
    private String passwordParameter = PASSWORD_KEY;
    private String captchaParameter = CAPTCHA_KEY;
    private boolean postOnly = true;
    /**
     * 登录是否需呀验证码
     */
    private Boolean needCaptcha = true;
    /**
     * 登录失败多少次才需呀验证码(小于等于0,总是需呀验证码)
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
        SecurityConfig.Login login = securityConfig.getLogin();
        if (login != null) {
            if (StringUtils.isNotBlank(login.getUsernameParameter())) {
                usernameParameter = login.getUsernameParameter();
            }
            if (StringUtils.isNotBlank(login.getPasswordParameter())) {
                passwordParameter = login.getPasswordParameter();
            }
            if (StringUtils.isNotBlank(login.getCaptchaParameter())) {
                captchaParameter = login.getCaptchaParameter();
            }
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
        this.setAuthenticationSuccessHandler(userLoginSuccessHandler);
        this.setAuthenticationFailureHandler(userLoginFailureHandler);
    }

    /**
     * 收集认证信息
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        // 需要防止用户重复登录
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authenticationTrustResolver.isRememberMe(authentication)) {
            // 已经登录成功了
            UserRes userRes = AuthenticationUtils.getUserRes(authentication);
            String token = request.getHeader(GenerateKeyService.JwtTokenHeaderKey);
            Jws<Claims> claimsJws = jwtTokenService.getClaimsJws(token);
            String jwtTokenKey = generateKeyService.getJwtTokenKey(claimsJws.getBody());
            JwtToken jwtToken = (JwtToken) redisTemplate.opsForValue().get(jwtTokenKey);
            assert jwtToken != null;
            String json = JacksonMapper.nonEmptyMapper().toJson(
                    new JwtLoginRes(
                            true,
                            "您已经登录成功了无须多次登录",
                            userRes,
                            token,
                            jwtToken.getRefreshToken()
                    )
            );
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
        // 获取用户登录信息
        String loginType;
        UserLoginToken userLoginToken;
        if (securityConfig.getLogin().getJsonDataSubmit()) {
            // 使用Json方式提交数据
            String json = IOUtils.toString(request.getReader());
            request.setAttribute(AttributeKeyConstant.Login_Data_Body_Request_Key, json);
            JSONObject object = new JSONObject(json);
            userLoginToken = new UserLoginToken(
                    object.optString(usernameParameter),
                    object.optString(passwordParameter),
                    object.optString(captchaParameter));
            loginType = object.optString(loginTypeParameter);
        } else {
            // 使用Parameter提交数据
            loginType = StringUtils.trimToEmpty(request.getParameter(loginTypeParameter));
            String username = StringUtils.trimToEmpty(request.getParameter(usernameParameter));
            String password = StringUtils.trimToEmpty(request.getParameter(passwordParameter));
            String captcha = StringUtils.trimToEmpty(request.getParameter(captchaParameter));
            userLoginToken = new UserLoginToken(username, password, captcha);
        }
        // 设置登录类型
        userLoginToken.setLoginType(loginType);
        // 设置用户 "details" 属性(设置请求IP和SessionID)
        userLoginToken.setDetails(authenticationDetailsSource.buildDetails(request));
        log.info("### 用户登录开始，构建UserLoginToken [{}]", userLoginToken.toString());
        // TODO  读取验证码 - 验证 不能使用Session
        if (needCaptcha) {
            Object loginFailCountStr = request.getSession().getAttribute(AttributeKeyConstant.Login_Fail_Count_Session_Key);
            int loginFailCount = 0;
            if (loginFailCountStr != null) {
                loginFailCount = NumberUtils.toInt(loginFailCountStr.toString(), 0);
            }
            if (loginFailCount > needCaptchaByLoginFailCount) {
                CaptchaInfo captchaInfo = (CaptchaInfo) request.getSession().getAttribute(AttributeKeyConstant.Login_Captcha_Session_Key);
                if (captchaInfo == null) {
                    throw new BadCaptchaException("验证码不存在");
                }
                if (captchaInfo.getEffectiveTime() > 0 && System.currentTimeMillis() - captchaInfo.getCreateTime() >= captchaInfo.getEffectiveTime()) {
                    throw new BadCaptchaException("验证码已过期");
                }
                if (!captchaInfo.getCode().equals(userLoginToken.getCaptcha())) {
                    throw new BadCaptchaException("验证码不匹配");
                }
                log.info("### 验证码校验通过");
            }
        }
        // 验证登录
        return this.getAuthenticationManager().authenticate(userLoginToken);
    }
}
