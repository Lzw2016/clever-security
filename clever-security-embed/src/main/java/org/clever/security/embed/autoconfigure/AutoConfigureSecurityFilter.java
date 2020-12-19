package org.clever.security.embed.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.embed.authentication.AuthenticationFilter;
import org.clever.security.embed.authentication.LoginFilter;
import org.clever.security.embed.authentication.LogoutFilter;
import org.clever.security.embed.authentication.login.AddJwtTokenExtData;
import org.clever.security.embed.authentication.login.LoadUser;
import org.clever.security.embed.authentication.login.VerifyLoginData;
import org.clever.security.embed.authentication.login.VerifyUserInfo;
import org.clever.security.embed.authentication.token.VerifyJwtToken;
import org.clever.security.embed.authorization.AuthorizationFilter;
import org.clever.security.embed.authorization.voter.AuthorizationVoter;
import org.clever.security.embed.collect.LoginDataCollect;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.context.SecurityContextRepository;
import org.clever.security.embed.handler.*;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * <pre>
 *   用户
 *   🡓
 *   LoginCaptchaFilter(登录图片验证码)
 *   LoginSmsValidateCodeFilter(登录短信验证码)
 *   LoginEmailValidateCodeFilter(登录邮箱验证码)
 *   GetScanCodeLoginFilter(获取扫码登录二维码)
 *   UserRegisterFilter(用户注册)
 *   PasswordRecoveryFilter(密码找回)
 *   🡓
 *   AuthenticationFilter(###认证)
 *   🡓
 *   ScanCodeLoginSupportFilter(扫描登录二维码、确认登录)
 *   🡓
 *   LoginFilter(###登录)
 *   🡓
 *   BindTelephoneFilter(手机号绑定/换绑)
 *   BindEmailFilter(邮箱绑定/换绑)
 *   🡓
 *   LogoutFilter(###登出)
 *   🡓
 *   AuthorizationFilter(###授权)
 *   🡓
 *   业务处理
 * </pre>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 12:39 <br/>
 */
@Order
@Configuration
@EnableConfigurationProperties({SecurityConfig.class})
@AutoConfigureAfter({AutoConfigureBaseBeans.class})
@Slf4j
public class AutoConfigureSecurityFilter {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;

    public AutoConfigureSecurityFilter(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

    @Bean("authenticationFilter")
    @ConditionalOnMissingBean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilter(
            List<VerifyJwtToken> verifyJwtTokenList,
            SecurityContextRepository securityContextRepository,
            List<AuthenticationSuccessHandler> authenticationSuccessHandlerList,
            final List<AuthenticationFailureHandler> authenticationFailureHandlerList) {
        AuthenticationFilter filter = new AuthenticationFilter(
                this.securityConfig,
                verifyJwtTokenList,
                securityContextRepository,
                authenticationSuccessHandlerList,
                authenticationFailureHandlerList
        );
        FilterRegistrationBean<AuthenticationFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setName("authenticationFilter");
        filterRegistration.setOrder(1);
        return filterRegistration;
    }

    @Bean("loginFilter")
    @ConditionalOnMissingBean
    public FilterRegistrationBean<LoginFilter> loginFilter(
            List<LoginDataCollect> loginDataCollectList,
            List<VerifyLoginData> verifyLoginDataList,
            List<LoadUser> loadUserList,
            List<VerifyUserInfo> verifyUserInfoList,
            List<AddJwtTokenExtData> addJwtTokenExtDataList,
            List<LoginSuccessHandler> loginSuccessHandlerList,
            List<LoginFailureHandler> loginFailureHandlerList,
            SecurityContextRepository securityContextRepository) {
        LoginFilter filter = new LoginFilter(
                this.securityConfig,
                loginDataCollectList,
                verifyLoginDataList,
                loadUserList,
                verifyUserInfoList,
                addJwtTokenExtDataList,
                loginSuccessHandlerList,
                loginFailureHandlerList,
                securityContextRepository
        );
        FilterRegistrationBean<LoginFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setName("loginFilter");
        filterRegistration.setOrder(1);
        return filterRegistration;
    }

    @Bean("logoutFilter")
    @ConditionalOnMissingBean
    public FilterRegistrationBean<LogoutFilter> logoutFilter(List<LogoutSuccessHandler> logoutSuccessHandlerList, List<LogoutFailureHandler> logoutFailureHandlerList) {
        LogoutFilter filter = new LogoutFilter(this.securityConfig, logoutSuccessHandlerList, logoutFailureHandlerList);
        FilterRegistrationBean<LogoutFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setName("logoutFilter");
        filterRegistration.setOrder(1);
        return filterRegistration;
    }

    @Bean("authorizationFilter")
    @ConditionalOnMissingBean
    public FilterRegistrationBean<AuthorizationFilter> authorizationFilter(
            List<AuthorizationVoter> authorizationVoterList,
            List<AuthorizationSuccessHandler> authorizationSuccessHandlerList,
            List<AuthorizationFailureHandler> authorizationFailureHandlerList) {
        AuthorizationFilter filter = new AuthorizationFilter(this.securityConfig, authorizationVoterList, authorizationSuccessHandlerList, authorizationFailureHandlerList);
        FilterRegistrationBean<AuthorizationFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setName("authorizationFilter");
        filterRegistration.setOrder(1);
        return filterRegistration;
    }
}
