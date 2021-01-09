package org.clever.security.embed.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.Constant;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.embed.authentication.*;
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
import org.clever.security.embed.extend.BindEmailFilter;
import org.clever.security.embed.extend.BindTelephoneFilter;
import org.clever.security.embed.extend.PasswordRecoveryFilter;
import org.clever.security.embed.register.UserRegisterFilter;
import org.clever.security.embed.handler.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *   用户
 *   🡓
 *   LoginCaptchaFilter(登录图片验证码)
 *   LoginSmsValidateCodeFilter(登录短信验证码)
 *   LoginEmailValidateCodeFilter(登录邮箱验证码)
 *   ScanCodeLoginFilter(获取扫码登录二维码)
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
    private static final int Base_Order = Ordered.HIGHEST_PRECEDENCE;
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;

    public AutoConfigureSecurityFilter(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

    /**
     * 登录图片验证码
     */
    @Bean("loginCaptchaFilter")
    @ConditionalOnMissingBean(name = "loginCaptchaFilter")
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "login.login-captcha.need-captcha", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<LoginCaptchaFilter> loginCaptchaFilter(ObjectProvider<LoginSupportClient> loginSupportClient) {
        LoginCaptchaFilter filter = new LoginCaptchaFilter(this.securityConfig, loginSupportClient.getIfAvailable());
        FilterRegistrationBean<LoginCaptchaFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        filterRegistration.addUrlPatterns(this.securityConfig.getLogin().getLoginCaptcha().getLoginCaptchaPath());
        filterRegistration.setName("loginCaptchaFilter");
        filterRegistration.setOrder(Base_Order + 1);
        return filterRegistration;
    }

    /**
     * 登录短信验证码
     */
    @Bean("loginSmsValidateCodeFilter")
    @ConditionalOnMissingBean(name = "loginSmsValidateCodeFilter")
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "login.sms-validate-code-login.enable", havingValue = "true")
    public FilterRegistrationBean<LoginSmsValidateCodeFilter> loginSmsValidateCodeFilter(ObjectProvider<LoginSupportClient> loginSupportClient) {
        LoginSmsValidateCodeFilter filter = new LoginSmsValidateCodeFilter(this.securityConfig, loginSupportClient.getIfAvailable());
        FilterRegistrationBean<LoginSmsValidateCodeFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        filterRegistration.addUrlPatterns(this.securityConfig.getLogin().getSmsValidateCodeLogin().getLoginSmsValidateCodePath());
        filterRegistration.setName("loginSmsValidateCodeFilter");
        filterRegistration.setOrder(Base_Order + 2);
        return filterRegistration;
    }

    /**
     * 登录邮箱验证码
     */
    @Bean("loginEmailValidateCodeFilter")
    @ConditionalOnMissingBean(name = "loginEmailValidateCodeFilter")
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "login.email-validate-code-login.enable", havingValue = "true")
    public FilterRegistrationBean<LoginEmailValidateCodeFilter> loginEmailValidateCodeFilter(ObjectProvider<LoginSupportClient> loginSupportClient) {
        LoginEmailValidateCodeFilter filter = new LoginEmailValidateCodeFilter(this.securityConfig, loginSupportClient.getIfAvailable());
        FilterRegistrationBean<LoginEmailValidateCodeFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        filterRegistration.addUrlPatterns(this.securityConfig.getLogin().getEmailValidateCodeLogin().getLoginEmailValidateCodePath());
        filterRegistration.setName("loginEmailValidateCodeFilter");
        filterRegistration.setOrder(Base_Order + 3);
        return filterRegistration;
    }

    /**
     * 获取扫码登录二维码
     */
    @Bean("scanCodeLoginFilter")
    @ConditionalOnMissingBean(name = "scanCodeLoginFilter")
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "login.scan-code-login.enable", havingValue = "true")
    public FilterRegistrationBean<ScanCodeLoginFilter> scanCodeLoginFilter(ObjectProvider<LoginSupportClient> loginSupportClient) {
        ScanCodeLoginFilter filter = new ScanCodeLoginFilter(this.securityConfig, loginSupportClient.getIfAvailable());
        FilterRegistrationBean<ScanCodeLoginFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        filterRegistration.addUrlPatterns(this.securityConfig.getLogin().getScanCodeLogin().getGetScanCodeLoginPath());
        filterRegistration.addUrlPatterns(this.securityConfig.getLogin().getScanCodeLogin().getScanCodeStatePath());
        filterRegistration.setName("scanCodeLoginFilter");
        filterRegistration.setOrder(Base_Order + 4);
        return filterRegistration;
    }

//    /**
//     * TODO 用户注册
//     */
//    @Bean("userRegisterFilter")
//    @ConditionalOnMissingBean(name = "userRegisterFilter")
//    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "???", havingValue = "true")
//    public FilterRegistrationBean<UserRegisterFilter> userRegisterFilter() {
//        UserRegisterFilter filter = new UserRegisterFilter(securityConfig);
//        FilterRegistrationBean<UserRegisterFilter> filterRegistration = new FilterRegistrationBean<>(filter);
//        filterRegistration.addUrlPatterns(this.securityConfig.getLogin().getEmailValidateCodeLogin().getLoginEmailValidateCodePath());
//        filterRegistration.setName("userRegisterFilter");
//        filterRegistration.setOrder(Base_Order + 5);
//        return filterRegistration;
//    }

    /**
     * TODO 密码找回
     */
    @Bean("passwordRecoveryFilter")
    @ConditionalOnMissingBean(name = "passwordRecoveryFilter")
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "???", havingValue = "true")
    public FilterRegistrationBean<PasswordRecoveryFilter> passwordRecoveryFilter() {
        PasswordRecoveryFilter filter = new PasswordRecoveryFilter();
        FilterRegistrationBean<PasswordRecoveryFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        filterRegistration.addUrlPatterns(this.securityConfig.getLogin().getEmailValidateCodeLogin().getLoginEmailValidateCodePath());
        filterRegistration.setName("passwordRecoveryFilter");
        filterRegistration.setOrder(Base_Order + 6);
        return filterRegistration;
    }

    /**
     * ###认证
     */
    @Bean("authenticationFilter")
    @ConditionalOnMissingBean(name = "authenticationFilter")
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilter(
            List<VerifyJwtToken> verifyJwtTokenList,
            SecurityContextRepository securityContextRepository,
            ObjectProvider<List<AuthenticationSuccessHandler>> authenticationSuccessHandlerList,
            ObjectProvider<List<AuthenticationFailureHandler>> authenticationFailureHandlerList,
            LoginSupportClient loginSupportClient) {
        AuthenticationFilter filter = new AuthenticationFilter(
                this.securityConfig,
                verifyJwtTokenList,
                securityContextRepository,
                authenticationSuccessHandlerList.getIfAvailable() == null ? new ArrayList<>() : authenticationSuccessHandlerList.getIfAvailable(),
                authenticationFailureHandlerList.getIfAvailable() == null ? new ArrayList<>() : authenticationFailureHandlerList.getIfAvailable(),
                loginSupportClient
        );
        FilterRegistrationBean<AuthenticationFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setName("authenticationFilter");
        filterRegistration.setOrder(Base_Order + 100);
        return filterRegistration;
    }

    /**
     * 扫描登录二维码、确认登录
     */
    @Bean("scanCodeLoginSupportFilter")
    @ConditionalOnMissingBean(name = "scanCodeLoginSupportFilter")
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "login.scan-code-login.enable", havingValue = "true")
    public FilterRegistrationBean<ScanCodeLoginSupportFilter> scanCodeLoginSupportFilter(ObjectProvider<LoginSupportClient> loginSupportClient) {
        ScanCodeLoginSupportFilter filter = new ScanCodeLoginSupportFilter(this.securityConfig, loginSupportClient.getIfAvailable());
        FilterRegistrationBean<ScanCodeLoginSupportFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        filterRegistration.addUrlPatterns(
                this.securityConfig.getLogin().getScanCodeLogin().getScanCodePath(),
                this.securityConfig.getLogin().getScanCodeLogin().getConfirmLoginPath()
        );
        filterRegistration.setName("scanCodeLoginSupportFilter");
        filterRegistration.setOrder(Base_Order + 100 + 1);
        return filterRegistration;
    }

    /**
     * ###登录
     */
    @Bean("loginFilter")
    @ConditionalOnMissingBean(name = "loginFilter")
    public FilterRegistrationBean<LoginFilter> loginFilter(
            List<LoginDataCollect> loginDataCollectList,
            List<VerifyLoginData> verifyLoginDataList,
            List<LoadUser> loadUserList,
            List<VerifyUserInfo> verifyUserInfoList,
            ObjectProvider<List<AddJwtTokenExtData>> addJwtTokenExtDataList,
            List<LoginSuccessHandler> loginSuccessHandlerList,
            List<LoginFailureHandler> loginFailureHandlerList,
            SecurityContextRepository securityContextRepository) {
        LoginFilter filter = new LoginFilter(
                this.securityConfig,
                loginDataCollectList,
                verifyLoginDataList,
                loadUserList,
                verifyUserInfoList,
                addJwtTokenExtDataList.getIfAvailable() == null ? new ArrayList<>() : addJwtTokenExtDataList.getIfAvailable(),
                loginSuccessHandlerList,
                loginFailureHandlerList,
                securityContextRepository
        );
        FilterRegistrationBean<LoginFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        filterRegistration.addUrlPatterns(this.securityConfig.getLogin().getLoginPath());
        filterRegistration.setName("loginFilter");
        filterRegistration.setOrder(Base_Order + 200);
        return filterRegistration;
    }

    /**
     * TODO 手机号绑定/换绑
     */
    @Bean("bindTelephoneFilter")
    @ConditionalOnMissingBean(name = "bindTelephoneFilter")
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "???", havingValue = "true")
    public FilterRegistrationBean<BindTelephoneFilter> bindTelephoneFilter() {
        BindTelephoneFilter filter = new BindTelephoneFilter();
        FilterRegistrationBean<BindTelephoneFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        filterRegistration.addUrlPatterns(this.securityConfig.getLogin().getEmailValidateCodeLogin().getLoginEmailValidateCodePath());
        filterRegistration.setName("bindTelephoneFilter");
        filterRegistration.setOrder(Base_Order + 200 + 1);
        return filterRegistration;
    }

    /**
     * TODO 邮箱绑定/换绑
     */
    @Bean("bindEmailFilter")
    @ConditionalOnMissingBean(name = "bindEmailFilter")
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "???", havingValue = "true")
    public FilterRegistrationBean<BindEmailFilter> bindEmailFilter() {
        BindEmailFilter filter = new BindEmailFilter();
        FilterRegistrationBean<BindEmailFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        filterRegistration.addUrlPatterns(this.securityConfig.getLogin().getEmailValidateCodeLogin().getLoginEmailValidateCodePath());
        filterRegistration.setName("bindEmailFilter");
        filterRegistration.setOrder(Base_Order + 200 + 2);
        return filterRegistration;
    }

    /**
     * ###登出
     */
    @Bean("logoutFilter")
    @ConditionalOnMissingBean(name = "logoutFilter")
    public FilterRegistrationBean<LogoutFilter> logoutFilter(
            List<LogoutSuccessHandler> logoutSuccessHandlerList,
            ObjectProvider<List<LogoutFailureHandler>> logoutFailureHandlerList) {
        LogoutFilter filter = new LogoutFilter(
                this.securityConfig,
                logoutSuccessHandlerList,
                logoutFailureHandlerList.getIfAvailable() == null ? new ArrayList<>() : logoutFailureHandlerList.getIfAvailable()
        );
        FilterRegistrationBean<LogoutFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        filterRegistration.addUrlPatterns(this.securityConfig.getLogout().getLogoutPath());
        filterRegistration.setName("logoutFilter");
        filterRegistration.setOrder(Base_Order + 300);
        return filterRegistration;
    }

    /**
     * ###授权
     */
    @Bean("authorizationFilter")
    @ConditionalOnMissingBean(name = "authorizationFilter")
    public FilterRegistrationBean<AuthorizationFilter> authorizationFilter(
            List<AuthorizationVoter> authorizationVoterList,
            ObjectProvider<List<AuthorizationSuccessHandler>> authorizationSuccessHandlerList,
            ObjectProvider<List<AuthorizationFailureHandler>> authorizationFailureHandlerList) {
        AuthorizationFilter filter = new AuthorizationFilter(
                this.securityConfig,
                authorizationVoterList,
                authorizationSuccessHandlerList.getIfAvailable() == null ? new ArrayList<>() : authorizationSuccessHandlerList.getIfAvailable(),
                authorizationFailureHandlerList.getIfAvailable() == null ? new ArrayList<>() : authorizationFailureHandlerList.getIfAvailable()
        );
        FilterRegistrationBean<AuthorizationFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setName("authorizationFilter");
        filterRegistration.setOrder(Base_Order + 400);
        return filterRegistration;
    }
}
