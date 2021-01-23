package org.clever.security.embed.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.Constant;
import org.clever.security.client.AuthSupportClient;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.client.RegisterSupportClient;
import org.clever.security.client.ServerAccessSupportClient;
import org.clever.security.embed.authentication.login.DefaultLoadUser;
import org.clever.security.embed.authentication.login.DefaultVerifyLoginData;
import org.clever.security.embed.authentication.login.DefaultVerifyUserInfo;
import org.clever.security.embed.authentication.token.DefaultVerifyJwtToken;
import org.clever.security.embed.authorization.voter.ControllerAuthorizationVoter;
import org.clever.security.embed.collect.*;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.context.DefaultSecurityContextRepository;
import org.clever.security.embed.handler.*;
import org.clever.security.embed.register.DefaultVerifyRegisterData;
import org.clever.security.embed.task.CacheServerAccessTokenTask;
import org.clever.security.third.client.WeChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/19 22:28 <br/>
 */
@Order
@Configuration
@Slf4j
public class AutoConfigureBaseBeans {
    // ------------------------------------------------------------------------------------------------------------------------------------------------- 收集用户登录信息

    @Bean("loginNamePasswordReqCollect")
    @ConditionalOnMissingBean(name = "loginNamePasswordReqCollect")
    public LoginNamePasswordReqCollect loginNamePasswordReqCollect() {
        return new LoginNamePasswordReqCollect();
    }

    @Bean("smsValidateCodeReqCollect")
    @ConditionalOnMissingBean(name = "smsValidateCodeReqCollect")
    public SmsValidateCodeReqCollect smsValidateCodeReqCollect() {
        return new SmsValidateCodeReqCollect();
    }

    @Bean("emailValidateCodeReqCollect")
    @ConditionalOnMissingBean(name = "emailValidateCodeReqCollect")
    public EmailValidateCodeReqCollect emailValidateCodeReqCollect() {
        return new EmailValidateCodeReqCollect();
    }

    @Bean("wechatSmallProgramReqCollect")
    @ConditionalOnMissingBean(name = "wechatSmallProgramReqCollect")
    public WechatSmallProgramReqCollect wechatSmallProgramReqCollect() {
        return new WechatSmallProgramReqCollect();
    }

    @Bean("scanCodeReqCollect")
    @ConditionalOnMissingBean(name = "scanCodeReqCollect")
    public ScanCodeReqCollect scanCodeReqCollect() {
        return new ScanCodeReqCollect();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------- 验证用户登录信息

    @Bean("defaultVerifyLoginData")
    @ConditionalOnMissingBean(name = "defaultVerifyLoginData")
    public DefaultVerifyLoginData defaultVerifyLoginData(LoginSupportClient loginSupportClient) {
        return new DefaultVerifyLoginData(loginSupportClient);
    }

    @Bean("defaultVerifyUserInfo")
    @ConditionalOnMissingBean(name = "defaultVerifyUserInfo")
    public DefaultVerifyUserInfo defaultVerifyUserInfo(LoginSupportClient loginSupportClient) {
        return new DefaultVerifyUserInfo(loginSupportClient);
    }

    @Bean("defaultVerifyJwtToken")
    @ConditionalOnMissingBean(name = "defaultVerifyJwtToken")
    public DefaultVerifyJwtToken defaultVerifyJwtToken(LoginSupportClient loginSupportClient) {
        return new DefaultVerifyJwtToken(loginSupportClient);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------- 登录、登出、授权Handler

    @Bean("defaultLoginSuccessHandler")
    @ConditionalOnMissingBean(name = "defaultLoginSuccessHandler")
    public DefaultLoginSuccessHandler defaultLoginSuccessHandler(LoginSupportClient loginSupportClient) {
        return new DefaultLoginSuccessHandler(loginSupportClient);
    }

    @Bean("defaultLoginFailureHandler")
    @ConditionalOnMissingBean(name = "defaultLoginFailureHandler")
    public DefaultLoginFailureHandler defaultLoginFailureHandler(LoginSupportClient loginSupportClient) {
        return new DefaultLoginFailureHandler(loginSupportClient);
    }

    @Bean("defaultLogoutSuccessHandler")
    @ConditionalOnMissingBean(name = "defaultLogoutSuccessHandler")
    public DefaultLogoutSuccessHandler defaultLogoutSuccessHandler(LoginSupportClient loginSupportClient) {
        return new DefaultLogoutSuccessHandler(loginSupportClient);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------- 加载UserInfo、SecurityContext、

    @Bean("defaultLoadUser")
    @ConditionalOnMissingBean(name = "defaultLoadUser")
    public DefaultLoadUser defaultLoadUser(LoginSupportClient loginSupportClient, ObjectProvider<WeChatClient> wechatClient) {
        return new DefaultLoadUser(loginSupportClient, wechatClient.getIfAvailable());
    }

    @Bean("defaultSecurityContextRepository")
    @ConditionalOnMissingBean(name = "defaultSecurityContextRepository")
    public DefaultSecurityContextRepository defaultSecurityContextRepository(AuthSupportClient authSupportClient) {
        return new DefaultSecurityContextRepository(authSupportClient);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------- Controller API接口授权

    @Bean("controllerAuthorizationVoter")
    @ConditionalOnMissingBean(name = "controllerAuthorizationVoter")
    public ControllerAuthorizationVoter controllerAuthorizationVoter(AuthSupportClient authSupportClient, RequestMappingHandlerMapping requestMappingHandlerMapping) {
        return new ControllerAuthorizationVoter(authSupportClient, requestMappingHandlerMapping);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------- 收集用户注册信息

    @Bean("loginNameRegisterReqCollect")
    @ConditionalOnMissingBean(name = "loginNameRegisterReqCollect")
    @Conditional(ConditionalOnEnableUserRegister.class)
    public LoginNameRegisterReqCollect loginNameRegisterReqCollect() {
        return new LoginNameRegisterReqCollect();
    }

    @Bean("smsRegisterReqCollect")
    @ConditionalOnMissingBean(name = "smsRegisterReqCollect")
    @Conditional(ConditionalOnEnableUserRegister.class)
    public SmsRegisterReqCollect smsRegisterReqCollect() {
        return new SmsRegisterReqCollect();
    }

    @Bean("emailRegisterReqCollect")
    @ConditionalOnMissingBean(name = "emailRegisterReqCollect")
    @Conditional(ConditionalOnEnableUserRegister.class)
    public EmailRegisterReqCollect emailRegisterReqCollect() {
        return new EmailRegisterReqCollect();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------- 验证用户注册信息

    @Bean("defaultVerifyRegisterData")
    @ConditionalOnMissingBean(name = "defaultVerifyRegisterData")
    @Conditional(ConditionalOnEnableUserRegister.class)
    public DefaultVerifyRegisterData defaultVerifyRegisterData(RegisterSupportClient registerSupportClient) {
        return new DefaultVerifyRegisterData(registerSupportClient);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------- 注册Handler

    @Bean("defaultRegisterFailureHandler")
    @ConditionalOnMissingBean(name = "defaultRegisterFailureHandler")
    @Conditional(ConditionalOnEnableUserRegister.class)
    public DefaultRegisterFailureHandler defaultRegisterFailureHandler(RegisterSupportClient registerSupportClient) {
        return new DefaultRegisterFailureHandler(registerSupportClient);
    }

    @Bean("defaultRegisterSuccessHandler")
    @ConditionalOnMissingBean(name = "defaultRegisterSuccessHandler")
    @Conditional(ConditionalOnEnableUserRegister.class)
    public DefaultRegisterSuccessHandler defaultRegisterSuccessHandler(RegisterSupportClient registerSupportClient) {
        return new DefaultRegisterSuccessHandler(registerSupportClient);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------- 定时任务调度

    @Bean("cacheServerAccessTokenTask")
    @ConditionalOnMissingBean(name = "cacheServerAccessTokenTask")
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "server-access.enable", havingValue = "true")
    public CacheServerAccessTokenTask cacheServerAccessTokenTask(SecurityConfig securityConfig, ServerAccessSupportClient serverAccessSupportClient) {
        return new CacheServerAccessTokenTask(securityConfig, serverAccessSupportClient);
    }
}
