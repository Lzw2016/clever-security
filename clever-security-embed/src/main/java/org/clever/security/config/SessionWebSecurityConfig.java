package org.clever.security.config;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.Constant;
import org.clever.security.authentication.UserLoginEntryPoint;
import org.clever.security.authentication.filter.UserLoginFilter;
import org.clever.security.config.model.LoginConfig;
import org.clever.security.config.model.RememberMeConfig;
import org.clever.security.handler.UserAccessDeniedHandler;
import org.clever.security.handler.UserLogoutSuccessHandler;
import org.clever.security.rememberme.UserLoginRememberMeServices;
import org.clever.security.service.GlobalUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import java.util.List;

/**
 * Session 登录配置
 * 作者： lzw<br/>
 * 创建时间：2018-03-14 14:45 <br/>
 */
@Configuration
@ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "loginModel", havingValue = "session")
@Slf4j
public class SessionWebSecurityConfig extends BaseWebSecurityConfig {

    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private UserLoginFilter userLoginFilter;
    @Autowired
    private GlobalUserDetailsService globalUserDetailsService;
    @Autowired
    private List<AuthenticationProvider> authenticationProviderList;
    @Autowired
    private UserLoginEntryPoint userLoginEntryPoint;
    @Autowired
    private UserLogoutSuccessHandler userLogoutSuccessHandler;
    @Autowired
    private UserAccessDeniedHandler userAccessDeniedHandler;
    @Autowired
    private AccessDecisionManager accessDecisionManager;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private SpringSessionBackedSessionRegistry sessionRegistry;
    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;
    @Autowired
    private RememberMeServices rememberMeServices;

    @Override
    PasswordEncoder getPasswordEncoder() {
        return bCryptPasswordEncoder;
    }

    @Override
    UserDetailsService getUserDetailsService() {
        return globalUserDetailsService;
    }

    @Override
    List<AuthenticationProvider> getAuthenticationProviderList() {
        return authenticationProviderList;
    }

    @Override
    SecurityConfig getSecurityConfig() {
        return securityConfig;
    }

    /**
     * 具体的权限控制规则配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 自定义登录 Filter --> UserLoginFilter
        http.addFilterAt(userLoginFilter, UsernamePasswordAuthenticationFilter.class);

//        http
//                .csrf().and()
//                .addFilter(new WebAsyncManagerIntegrationFilter())
//                .exceptionHandling().and()
//                .headers().and()
//                .sessionManagement().and()
//                .securityContext().and()
//                .requestCache().and()
//                .anonymous().and()
//                .servletApi().and()
//                .apply(new DefaultLoginPageConfigurer<>()).and()
//                .logout();
//        ClassLoader classLoader = this.getApplicationContext().getClassLoader();
//        List<AbstractHttpConfigurer> defaultHttpConfigurers = SpringFactoriesLoader.loadFactories(AbstractHttpConfigurer.class, classLoader);
//        for(AbstractHttpConfigurer configurer : defaultHttpConfigurers) {
//            http.apply(configurer);
//        }

        // 过滤器配置
        http
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(userLoginEntryPoint).accessDeniedHandler(userAccessDeniedHandler)
                .and()
                .authorizeRequests().anyRequest().authenticated().accessDecisionManager(accessDecisionManager)
                .and()
                .formLogin().disable()
                .logout().logoutUrl(securityConfig.getLogout().getLogoutUrl()).logoutSuccessHandler(userLogoutSuccessHandler).permitAll()
        ;
        // 设置"记住我功能配置"
        RememberMeConfig rememberMe = securityConfig.getRememberMe();
        if (rememberMe != null && rememberMe.getEnable()) {
            http.rememberMe()
                    .rememberMeServices(rememberMeServices)
                    .alwaysRemember(rememberMe.getAlwaysRemember())
                    .tokenValiditySeconds((int) rememberMe.getValidity().getSeconds())
                    .rememberMeParameter(rememberMe.getRememberMeParameterName())
                    .rememberMeCookieName(UserLoginRememberMeServices.REMEMBER_ME)
                    .key(UserLoginRememberMeServices.REMEMBER_ME_KEY)
            ;
        }
        // 登录并发控制
        LoginConfig login = securityConfig.getLogin();
        if (login.getConcurrentLoginCount() != null) {
            int concurrentLoginCount = login.getConcurrentLoginCount() <= 0 ? -1 : login.getConcurrentLoginCount();
            boolean notAllowAfterLogin = false;
            if (login.getNotAllowAfterLogin() != null) {
                notAllowAfterLogin = login.getNotAllowAfterLogin();
            }
            http.sessionManagement()
                    .maximumSessions(concurrentLoginCount)
                    .maxSessionsPreventsLogin(notAllowAfterLogin)
                    .sessionRegistry(sessionRegistry)
                    .expiredSessionStrategy(sessionInformationExpiredStrategy)
            ;
        }
        log.info("### HttpSecurity 配置完成!");
    }
}