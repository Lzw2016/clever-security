package org.clever.security.embed.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.AuthSupportClient;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.client.WeChatClient;
import org.clever.security.embed.authentication.login.DefaultAddJwtTokenExtData;
import org.clever.security.embed.authentication.login.DefaultLoadUser;
import org.clever.security.embed.authentication.login.DefaultVerifyLoginData;
import org.clever.security.embed.authentication.login.DefaultVerifyUserInfo;
import org.clever.security.embed.authentication.token.DefaultVerifyJwtToken;
import org.clever.security.embed.authorization.voter.ControllerAuthorizationVoter;
import org.clever.security.embed.collect.*;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.context.DefaultSecurityContextRepository;
import org.clever.security.embed.crypto.BCryptPasswordEncoder;
import org.clever.security.embed.crypto.PasswordEncoder;
import org.clever.security.embed.handler.DefaultLoginFailureHandler;
import org.clever.security.embed.handler.DefaultLoginSuccessHandler;
import org.clever.security.embed.handler.DefaultLogoutSuccessHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.security.SecureRandom;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/19 22:28 <br/>
 */
@Order
@Configuration
@EnableConfigurationProperties({SecurityConfig.class})
@Slf4j
public class AutoConfigureBaseBeans {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;

    public AutoConfigureBaseBeans(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

    @Bean("passwordEncoder")
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------- 收集用户登录信息

    @Bean("loginNamePasswordReqCollect")
    @ConditionalOnMissingBean
    public LoginNamePasswordReqCollect loginNamePasswordReqCollect() {
        return new LoginNamePasswordReqCollect();
    }

    @Bean("smsValidateCodeReqCollect")
    @ConditionalOnMissingBean
    public SmsValidateCodeReqCollect smsValidateCodeReqCollect() {
        return new SmsValidateCodeReqCollect();
    }

    @Bean("emailValidateCodeReqCollect")
    @ConditionalOnMissingBean
    public EmailValidateCodeReqCollect emailValidateCodeReqCollect() {
        return new EmailValidateCodeReqCollect();
    }

    @Bean("wechatSmallProgramReqCollect")
    @ConditionalOnMissingBean
    public WechatSmallProgramReqCollect wechatSmallProgramReqCollect() {
        return new WechatSmallProgramReqCollect();
    }

    @Bean("scanCodeReqCollect")
    @ConditionalOnMissingBean
    public ScanCodeReqCollect scanCodeReqCollect() {
        return new ScanCodeReqCollect();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------- 收集用户登录信息

    @Bean("defaultVerifyLoginData")
    @ConditionalOnMissingBean
    public DefaultVerifyLoginData defaultVerifyLoginData(LoginSupportClient loginSupportClient) {
        return new DefaultVerifyLoginData(loginSupportClient);
    }

    @Bean("defaultLoadUser")
    @ConditionalOnMissingBean
    public DefaultLoadUser defaultLoadUser(LoginSupportClient loginSupportClient, WeChatClient wechatClient) {
        return new DefaultLoadUser(loginSupportClient, wechatClient);
    }

    @Bean("defaultVerifyUserInfo")
    @ConditionalOnMissingBean
    public DefaultVerifyUserInfo defaultVerifyUserInfo(PasswordEncoder passwordEncoder, LoginSupportClient loginSupportClient) {
        return new DefaultVerifyUserInfo(passwordEncoder, loginSupportClient);
    }

    @Bean("defaultAddJwtTokenExtData")
    @ConditionalOnMissingBean
    public DefaultAddJwtTokenExtData defaultAddJwtTokenExtData() {
        return new DefaultAddJwtTokenExtData();
    }

    @Bean("defaultLoginSuccessHandler")
    @ConditionalOnMissingBean
    public DefaultLoginSuccessHandler defaultLoginSuccessHandler(LoginSupportClient loginSupportClient) {
        return new DefaultLoginSuccessHandler(loginSupportClient);
    }

    @Bean("defaultLoginFailureHandler")
    @ConditionalOnMissingBean
    public DefaultLoginFailureHandler defaultLoginFailureHandler(LoginSupportClient loginSupportClient) {
        return new DefaultLoginFailureHandler(loginSupportClient);
    }

    @Bean("defaultSecurityContextRepository")
    @ConditionalOnMissingBean
    public DefaultSecurityContextRepository defaultSecurityContextRepository(AuthSupportClient authSupportClient) {
        return new DefaultSecurityContextRepository(authSupportClient);
    }

    @Bean("defaultVerifyJwtToken")
    @ConditionalOnMissingBean
    public DefaultVerifyJwtToken defaultVerifyJwtToken(LoginSupportClient loginSupportClient) {
        return new DefaultVerifyJwtToken(loginSupportClient);
    }

    @Bean("defaultLogoutSuccessHandler")
    @ConditionalOnMissingBean
    public DefaultLogoutSuccessHandler defaultLogoutSuccessHandler(LoginSupportClient loginSupportClient) {
        return new DefaultLogoutSuccessHandler(loginSupportClient);
    }

    @Bean("controllerAuthorizationVoter")
    @ConditionalOnMissingBean
    public ControllerAuthorizationVoter controllerAuthorizationVoter(AuthSupportClient authSupportClient, RequestMappingHandlerMapping requestMappingHandlerMapping) {
        return new ControllerAuthorizationVoter(authSupportClient, requestMappingHandlerMapping);
    }
}
