package org.clever.security.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.security.authentication.rememberme.UserLoginRememberMeServices;
import org.clever.security.authentication.rememberme.UserLoginTokenRepository;
import org.clever.security.authorization.RequestAccessDecisionVoter;
import org.clever.security.service.LoginUserDetailsService;
import org.clever.security.strategy.SessionExpiredStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-17 9:49 <br/>
 */
//@EnableFeignClients(basePackages = {"org.clever.security.client"})
@Configuration
@Slf4j
public class ApplicationSecurityBean {

    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private RequestAccessDecisionVoter requestAccessDecisionVoter;
    @Autowired
    private SessionRegistry sessionRegistry;

    /**
     * 授权校验
     */
    @Bean
    protected AccessDecisionManager accessDecisionManager() {
        // WebExpressionVoter RoleVoter AuthenticatedVoter
        List<AccessDecisionVoter<?>> decisionVoters = Collections.singletonList(requestAccessDecisionVoter);
        AccessDecisionManager accessDecisionManager = new AffirmativeBased(decisionVoters);
        // accessDecisionManager.
        return accessDecisionManager;
    }

    /**
     * 密码处理
     */
    @Bean
    protected BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    }

    /**
     * 登录并发处理
     */
    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        SecurityConfig.Login login = securityConfig.getLogin();
        if (login.getConcurrentLoginCount() == null) {
            return new NullAuthenticatedSessionStrategy();
        }
        int concurrentLoginCount = login.getConcurrentLoginCount() <= 0 ? -1 : login.getConcurrentLoginCount();
        boolean notAllowAfterLogin = false;
        if (login.getNotAllowAfterLogin() != null) {
            notAllowAfterLogin = login.getNotAllowAfterLogin();
        }
        ConcurrentSessionControlAuthenticationStrategy sessionAuthenticationStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry);
        sessionAuthenticationStrategy.setMaximumSessions(concurrentLoginCount);
        sessionAuthenticationStrategy.setExceptionIfMaximumExceeded(notAllowAfterLogin);
        // sessionAuthenticationStrategy.setMessageSource();
        return sessionAuthenticationStrategy;
    }

    /**
     * Session 过期处理
     */
    @Bean
    protected SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        SessionExpiredStrategy sessionExpiredStrategy = new SessionExpiredStrategy();
        sessionExpiredStrategy.setNeedRedirect(StringUtils.isNotBlank(securityConfig.getSessionExpiredRedirectUrl()));
        sessionExpiredStrategy.setDestinationUrl(securityConfig.getSessionExpiredRedirectUrl());
        return sessionExpiredStrategy;
    }

    /**
     * 实现的记住我的功能, 不使用 Spring Session 提供的 SpringSessionRememberMeServices
     */
    @Bean
    protected RememberMeServices rememberMeServices(LoginUserDetailsService userDetailsService, UserLoginTokenRepository userLoginTokenRepository) {
        SecurityConfig.RememberMe rememberMe = securityConfig.getRememberMe();
        if (rememberMe == null || rememberMe.getEnable() == null || !rememberMe.getEnable()) {
            return new NullRememberMeServices();
        }
        UserLoginRememberMeServices rememberMeServices = new UserLoginRememberMeServices(
                "remember-me-key",
                userDetailsService,
                userLoginTokenRepository);
        rememberMeServices.setAlwaysRemember(rememberMe.getAlwaysRemember());
        rememberMeServices.setParameter(rememberMe.getRememberMeParameterName());
        rememberMeServices.setTokenValiditySeconds(rememberMe.getValiditySeconds());
        rememberMeServices.setCookieName("remember-me");
//        rememberMeServices.setTokenLength();
//        rememberMeServices.setSeriesLength();
//        rememberMeServices.setUserDetailsChecker();
//        rememberMeServices.setUseSecureCookie();
//        rememberMeServices.setCookieDomain();
        return rememberMeServices;
    }
}
