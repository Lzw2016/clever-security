package org.clever.security.embed.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.security.Constant;
import org.clever.security.client.*;
import org.clever.security.embed.authentication.*;
import org.clever.security.embed.authentication.login.AddJwtTokenExtData;
import org.clever.security.embed.authentication.login.LoadUser;
import org.clever.security.embed.authentication.login.VerifyLoginData;
import org.clever.security.embed.authentication.login.VerifyUserInfo;
import org.clever.security.embed.authentication.token.VerifyJwtToken;
import org.clever.security.embed.authorization.AuthorizationFilter;
import org.clever.security.embed.authorization.voter.AuthorizationVoter;
import org.clever.security.embed.collect.LoginDataCollect;
import org.clever.security.embed.collect.RegisterDataCollect;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.*;
import org.clever.security.embed.context.SecurityContextRepository;
import org.clever.security.embed.extend.BindEmailFilter;
import org.clever.security.embed.extend.BindTelephoneFilter;
import org.clever.security.embed.extend.PasswordRecoveryFilter;
import org.clever.security.embed.extend.UpdatePasswordFilter;
import org.clever.security.embed.handler.*;
import org.clever.security.embed.register.RegisterCaptchaFilter;
import org.clever.security.embed.register.UserRegisterFilter;
import org.clever.security.embed.register.VerifyRegisterData;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *   用户
 *   🡓
 *   LoginCaptchaFilter(获取登录图片验证码)
 *   LoginSmsValidateCodeFilter(登录短信验证码)
 *   LoginEmailValidateCodeFilter(登录邮箱验证码)
 *   ScanCodeLoginFilter(获取扫码登录二维码)
 *   RegisterCaptchaFilter(获取注册验证码)
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
 *   ResetPasswordFilter(设置/修改密码)
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
    public FilterRegistrationBean<LoginCaptchaFilter> loginCaptchaFilter(LoginSupportClient loginSupportClient) {
        LoginCaptchaFilter filter = new LoginCaptchaFilter(securityConfig, loginSupportClient);
        FilterRegistrationBean<LoginCaptchaFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        filterRegistration.addUrlPatterns(securityConfig.getLogin().getLoginCaptcha().getLoginCaptchaPath());
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
    public FilterRegistrationBean<LoginSmsValidateCodeFilter> loginSmsValidateCodeFilter(LoginSupportClient loginSupportClient) {
        LoginSmsValidateCodeFilter filter = new LoginSmsValidateCodeFilter(securityConfig, loginSupportClient);
        FilterRegistrationBean<LoginSmsValidateCodeFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        filterRegistration.addUrlPatterns(securityConfig.getLogin().getSmsValidateCodeLogin().getLoginSmsValidateCodePath());
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
    public FilterRegistrationBean<LoginEmailValidateCodeFilter> loginEmailValidateCodeFilter(LoginSupportClient loginSupportClient) {
        LoginEmailValidateCodeFilter filter = new LoginEmailValidateCodeFilter(securityConfig, loginSupportClient);
        FilterRegistrationBean<LoginEmailValidateCodeFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        filterRegistration.addUrlPatterns(securityConfig.getLogin().getEmailValidateCodeLogin().getLoginEmailValidateCodePath());
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
    public FilterRegistrationBean<ScanCodeLoginFilter> scanCodeLoginFilter(LoginSupportClient loginSupportClient) {
        ScanCodeLoginFilter filter = new ScanCodeLoginFilter(securityConfig, loginSupportClient);
        FilterRegistrationBean<ScanCodeLoginFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        filterRegistration.addUrlPatterns(securityConfig.getLogin().getScanCodeLogin().getGetScanCodeLoginPath());
        filterRegistration.addUrlPatterns(securityConfig.getLogin().getScanCodeLogin().getScanCodeStatePath());
        filterRegistration.setName("scanCodeLoginFilter");
        filterRegistration.setOrder(Base_Order + 4);
        return filterRegistration;
    }

    /**
     * 用户注册验证码
     */
    @Bean("registerCaptchaFilter")
    @ConditionalOnMissingBean(name = "registerCaptchaFilter")
    @Conditional(ConditionalOnEnableUserRegister.class)
    public FilterRegistrationBean<RegisterCaptchaFilter> registerCaptchaFilter(RegisterSupportClient registerSupportClient) {
        UserRegisterConfig register = securityConfig.getRegister();
        Assert.notNull(register, "register配置不能为null");
        LoginNameRegisterConfig loginNameRegister = register.getLoginNameRegister();
        SmsRegisterConfig smsRegister = register.getSmsRegister();
        EmailRegisterConfig emailRegister = register.getEmailRegister();
        RegisterCaptchaFilter filter = new RegisterCaptchaFilter(securityConfig, registerSupportClient);
        FilterRegistrationBean<RegisterCaptchaFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        if (loginNameRegister != null && StringUtils.isNotBlank(loginNameRegister.getRegisterCaptchaPath())) {
            filterRegistration.addUrlPatterns(loginNameRegister.getRegisterCaptchaPath());
        }
        if (smsRegister != null) {
            if (StringUtils.isNotBlank(smsRegister.getRegisterCaptchaPath())) {
                filterRegistration.addUrlPatterns(smsRegister.getRegisterCaptchaPath());
            }
            if (StringUtils.isNotBlank(smsRegister.getRegisterSmsValidateCodePath())) {
                filterRegistration.addUrlPatterns(smsRegister.getRegisterSmsValidateCodePath());
            }
        }
        if (emailRegister != null) {
            if (StringUtils.isNotBlank(emailRegister.getRegisterCaptchaPath())) {
                filterRegistration.addUrlPatterns(emailRegister.getRegisterCaptchaPath());
            }
            if (StringUtils.isNotBlank(emailRegister.getRegisterEmailValidateCodePath())) {
                filterRegistration.addUrlPatterns(emailRegister.getRegisterEmailValidateCodePath());
            }
        }
        filterRegistration.setName("registerCaptchaFilter");
        filterRegistration.setOrder(Base_Order + 5);
        return filterRegistration;
    }

    /**
     * 用户注册
     */
    @Bean("userRegisterFilter")
    @ConditionalOnMissingBean(name = "userRegisterFilter")
    @Conditional(ConditionalOnEnableUserRegister.class)
    public FilterRegistrationBean<UserRegisterFilter> userRegisterFilter(
            List<RegisterDataCollect> registerDataCollectList,
            List<VerifyRegisterData> verifyRegisterDataList,
            ObjectProvider<List<RegisterSuccessHandler>> registerSuccessHandlerList,
            ObjectProvider<List<RegisterFailureHandler>> registerFailureHandlerList,
            RegisterSupportClient registerSupportClient) {
        UserRegisterFilter filter = new UserRegisterFilter(
                securityConfig,
                registerDataCollectList,
                verifyRegisterDataList,
                registerSuccessHandlerList.getIfAvailable() == null ? new ArrayList<>() : registerSuccessHandlerList.getIfAvailable(),
                registerFailureHandlerList.getIfAvailable() == null ? new ArrayList<>() : registerFailureHandlerList.getIfAvailable(),
                registerSupportClient
        );
        FilterRegistrationBean<UserRegisterFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        filterRegistration.addUrlPatterns(securityConfig.getRegister().getRegisterPath());
        filterRegistration.setName("userRegisterFilter");
        filterRegistration.setOrder(Base_Order + 6);
        return filterRegistration;
    }

    /**
     * 密码找回
     */
    @Bean("passwordRecoveryFilter")
    @ConditionalOnMissingBean(name = "passwordRecoveryFilter")
    @Conditional(ConditionalOnEnablePasswordRecovery.class)
    public FilterRegistrationBean<PasswordRecoveryFilter> passwordRecoveryFilter(PasswordRecoverySupportClient passwordRecoverySupportClient) {
        PasswordRecoveryFilter filter = new PasswordRecoveryFilter(securityConfig, passwordRecoverySupportClient);
        FilterRegistrationBean<PasswordRecoveryFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        PasswordRecoveryConfig passwordRecovery = securityConfig.getPasswordRecovery();
        if (passwordRecovery != null) {
            if (StringUtils.isNotBlank(passwordRecovery.getPasswordRecoveryPath())) {
                filterRegistration.addUrlPatterns(passwordRecovery.getPasswordRecoveryPath());
            }
            PasswordSmsRecoveryConfig smsRecovery = passwordRecovery.getSmsRecovery();
            if (smsRecovery != null && StringUtils.isNotBlank(smsRecovery.getCaptchaPath())) {
                filterRegistration.addUrlPatterns(smsRecovery.getCaptchaPath());
            }
            if (smsRecovery != null && StringUtils.isNotBlank(smsRecovery.getSmsValidateCodePath())) {
                filterRegistration.addUrlPatterns(smsRecovery.getSmsValidateCodePath());
            }
            PasswordEmailRecoveryConfig emailRecovery = passwordRecovery.getEmailRecovery();
            if (emailRecovery != null && StringUtils.isNotBlank(emailRecovery.getCaptchaPath())) {
                filterRegistration.addUrlPatterns(emailRecovery.getCaptchaPath());
            }
            if (emailRecovery != null && StringUtils.isNotBlank(emailRecovery.getEmailValidateCodePath())) {
                filterRegistration.addUrlPatterns(emailRecovery.getEmailValidateCodePath());
            }
        }
        filterRegistration.setName("passwordRecoveryFilter");
        filterRegistration.setOrder(Base_Order + 7);
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
                securityConfig,
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
    public FilterRegistrationBean<ScanCodeLoginSupportFilter> scanCodeLoginSupportFilter(LoginSupportClient loginSupportClient) {
        ScanCodeLoginSupportFilter filter = new ScanCodeLoginSupportFilter(securityConfig, loginSupportClient);
        FilterRegistrationBean<ScanCodeLoginSupportFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        filterRegistration.addUrlPatterns(
                securityConfig.getLogin().getScanCodeLogin().getScanCodePath(),
                securityConfig.getLogin().getScanCodeLogin().getConfirmLoginPath()
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
                securityConfig,
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
        filterRegistration.addUrlPatterns(securityConfig.getLogin().getLoginPath());
        filterRegistration.setName("loginFilter");
        filterRegistration.setOrder(Base_Order + 200);
        return filterRegistration;
    }

    /**
     * 手机号绑定/换绑
     */
    @Bean("bindTelephoneFilter")
    @ConditionalOnMissingBean(name = "bindTelephoneFilter")
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "bind-telephone.enable", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<BindTelephoneFilter> bindTelephoneFilter(BindSupportClient bindSupportClient) {
        BindTelephoneConfig bindTelephone = securityConfig.getBindTelephone();
        BindTelephoneFilter filter = new BindTelephoneFilter(securityConfig, bindSupportClient);
        FilterRegistrationBean<BindTelephoneFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        if (bindTelephone != null && StringUtils.isNotBlank(bindTelephone.getBindTelephonePath())) {
            filterRegistration.addUrlPatterns(bindTelephone.getBindTelephonePath());
        }
        if (bindTelephone != null && StringUtils.isNotBlank(bindTelephone.getCaptchaPath())) {
            filterRegistration.addUrlPatterns(bindTelephone.getCaptchaPath());
        }
        if (bindTelephone != null && StringUtils.isNotBlank(bindTelephone.getSmsValidateCodePath())) {
            filterRegistration.addUrlPatterns(bindTelephone.getSmsValidateCodePath());
        }
        filterRegistration.setName("bindTelephoneFilter");
        filterRegistration.setOrder(Base_Order + 200 + 1);
        return filterRegistration;
    }

    /**
     * 邮箱绑定/换绑
     */
    @Bean("bindEmailFilter")
    @ConditionalOnMissingBean(name = "bindEmailFilter")
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "bind-email.enable", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<BindEmailFilter> bindEmailFilter(BindSupportClient bindSupportClient) {
        BindEmailConfig bindEmail = securityConfig.getBindEmail();
        BindEmailFilter filter = new BindEmailFilter(securityConfig, bindSupportClient);
        FilterRegistrationBean<BindEmailFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        if (bindEmail != null && StringUtils.isNotBlank(bindEmail.getBindEmailPath())) {
            filterRegistration.addUrlPatterns(bindEmail.getBindEmailPath());
        }
        if (bindEmail != null && StringUtils.isNotBlank(bindEmail.getCaptchaPath())) {
            filterRegistration.addUrlPatterns(bindEmail.getCaptchaPath());
        }
        if (bindEmail != null && StringUtils.isNotBlank(bindEmail.getEmailValidateCodePath())) {
            filterRegistration.addUrlPatterns(bindEmail.getEmailValidateCodePath());
        }
        filterRegistration.setName("bindEmailFilter");
        filterRegistration.setOrder(Base_Order + 200 + 2);
        return filterRegistration;
    }

    /**
     * 设置/修改密码
     */
    @Bean("updatePasswordFilter")
    @ConditionalOnMissingBean(name = "updatePasswordFilter")
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "update-password.enable", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<UpdatePasswordFilter> updatePasswordFilter(UpdatePasswordSupportClient updatePasswordSupportClient) {
        UpdatePasswordConfig updatePassword = securityConfig.getUpdatePassword();
        UpdatePasswordFilter filter = new UpdatePasswordFilter(securityConfig, updatePasswordSupportClient);
        FilterRegistrationBean<UpdatePasswordFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        if (updatePassword != null && StringUtils.isNotBlank(updatePassword.getCaptchaPath())) {
            filterRegistration.addUrlPatterns(updatePassword.getCaptchaPath());
        }
        if (updatePassword != null && StringUtils.isNotBlank(updatePassword.getSmsValidateCodePath())) {
            filterRegistration.addUrlPatterns(updatePassword.getSmsValidateCodePath());
        }
        if (updatePassword != null && StringUtils.isNotBlank(updatePassword.getEmailValidateCodePath())) {
            filterRegistration.addUrlPatterns(updatePassword.getEmailValidateCodePath());
        }
        if (updatePassword != null && StringUtils.isNotBlank(updatePassword.getInitPasswordPath())) {
            filterRegistration.addUrlPatterns(updatePassword.getInitPasswordPath());
        }
        if (updatePassword != null && StringUtils.isNotBlank(updatePassword.getUpdatePasswordPath())) {
            filterRegistration.addUrlPatterns(updatePassword.getUpdatePasswordPath());
        }
        filterRegistration.setName("updatePasswordFilter");
        filterRegistration.setOrder(Base_Order + 200 + 3);
        return filterRegistration;
    }

    /**
     * ###登出
     */
    @Bean("logoutFilter")
    @ConditionalOnMissingBean(name = "logoutFilter")
    public FilterRegistrationBean<LogoutFilter> logoutFilter(
            ObjectProvider<List<LogoutSuccessHandler>> logoutSuccessHandlerList,
            ObjectProvider<List<LogoutFailureHandler>> logoutFailureHandlerList) {
        LogoutFilter filter = new LogoutFilter(
                securityConfig,
                logoutSuccessHandlerList.getIfAvailable() == null ? new ArrayList<>() : logoutSuccessHandlerList.getIfAvailable(),
                logoutFailureHandlerList.getIfAvailable() == null ? new ArrayList<>() : logoutFailureHandlerList.getIfAvailable()
        );
        FilterRegistrationBean<LogoutFilter> filterRegistration = new FilterRegistrationBean<>(filter);
        filterRegistration.addUrlPatterns(securityConfig.getLogout().getLogoutPath());
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
                securityConfig,
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
