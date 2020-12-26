package org.clever.security.embed.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.AuthSupportClient;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.embed.authentication.login.DefaultLoadUser;
import org.clever.security.embed.authentication.login.DefaultVerifyLoginData;
import org.clever.security.embed.authentication.login.DefaultVerifyUserInfo;
import org.clever.security.embed.authentication.token.DefaultVerifyJwtToken;
import org.clever.security.embed.authorization.voter.ControllerAuthorizationVoter;
import org.clever.security.embed.collect.*;
import org.clever.security.embed.context.DefaultSecurityContextRepository;
import org.clever.security.embed.crypto.BCryptPasswordEncoder;
import org.clever.security.embed.crypto.PasswordEncoder;
import org.clever.security.embed.handler.DefaultLoginFailureHandler;
import org.clever.security.embed.handler.DefaultLoginSuccessHandler;
import org.clever.security.embed.handler.DefaultLogoutSuccessHandler;
import org.clever.security.third.client.WeChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
@Slf4j
public class AutoConfigureBaseBeans {
    @Bean("passwordEncoder")
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------- 收集用户登录信息

    @Bean("loginNamePasswordReqCollect")
    @ConditionalOnMissingBean(name = "")
    public LoginNamePasswordReqCollect loginNamePasswordReqCollect() {
        return new LoginNamePasswordReqCollect();
    }

    @Bean("smsValidateCodeReqCollect")
    @ConditionalOnMissingBean(name = "")
    public SmsValidateCodeReqCollect smsValidateCodeReqCollect() {
        return new SmsValidateCodeReqCollect();
    }

    @Bean("emailValidateCodeReqCollect")
    @ConditionalOnMissingBean(name = "")
    public EmailValidateCodeReqCollect emailValidateCodeReqCollect() {
        return new EmailValidateCodeReqCollect();
    }

    @Bean("wechatSmallProgramReqCollect")
    @ConditionalOnMissingBean(name = "")
    public WechatSmallProgramReqCollect wechatSmallProgramReqCollect() {
        return new WechatSmallProgramReqCollect();
    }

    @Bean("scanCodeReqCollect")
    @ConditionalOnMissingBean(name = "")
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
    public DefaultVerifyUserInfo defaultVerifyUserInfo(PasswordEncoder passwordEncoder, LoginSupportClient loginSupportClient) {
        return new DefaultVerifyUserInfo(passwordEncoder, loginSupportClient);
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
}
