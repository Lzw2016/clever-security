package org.clever.security.config;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.Constant;
import org.clever.security.authentication.UserLoginEntryPoint;
import org.clever.security.authentication.filter.UserLoginFilter;
import org.clever.security.handler.UserAccessDeniedHandler;
import org.clever.security.handler.UserLogoutHandler;
import org.clever.security.handler.UserLogoutSuccessHandler;
import org.clever.security.repository.JwtRedisSecurityContextRepository;
import org.clever.security.service.GlobalUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

import java.util.List;

/**
 * Jwt Token 登录配置
 * 作者： lzw<br/>
 * 创建时间：2018-03-14 14:45 <br/>
 */
@Configuration
@ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "loginModel", havingValue = "jwt")
@Slf4j
public class JwtWebSecurityConfig extends BaseWebSecurityConfig {

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
    private UserLogoutHandler userLogoutHandler;
    @Autowired
    private UserLogoutSuccessHandler userLogoutSuccessHandler;
    @Autowired
    private UserAccessDeniedHandler userAccessDeniedHandler;
    // @Autowired
    // private AccessDecisionManager accessDecisionManager;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JwtRedisSecurityContextRepository jwtRedisSecurityContextRepository;

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
        // Jwt Token不需要使用HttpSession
        http.setSharedObject(SecurityContextRepository.class, jwtRedisSecurityContextRepository);
        http.setSharedObject(RequestCache.class, new NullRequestCache());

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
                .sessionManagement().disable()
                .exceptionHandling().authenticationEntryPoint(userLoginEntryPoint).accessDeniedHandler(userAccessDeniedHandler)
                .and()
                .authorizeRequests().anyRequest().authenticated()//.accessDecisionManager(accessDecisionManager)
                .and()
                .formLogin().disable()
                .logout().logoutUrl(securityConfig.getLogout().getLogoutUrl()).addLogoutHandler(userLogoutHandler).logoutSuccessHandler(userLogoutSuccessHandler).permitAll()
        ;
        // 禁用"记住我功能配置"(Token的记住我功能仅仅只是Token过期时间加长)
        http.rememberMe().disable();
        log.info("### HttpSecurity 配置完成!");
    }
}