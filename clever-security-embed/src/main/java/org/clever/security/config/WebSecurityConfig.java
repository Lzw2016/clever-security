package org.clever.security.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.security.authentication.UserLoginEntryPoint;
import org.clever.security.authentication.UserLoginTokenAuthenticationProvider;
import org.clever.security.authentication.rememberme.UserLoginRememberMeServices;
import org.clever.security.filter.UserLoginFilter;
import org.clever.security.handler.UserAccessDeniedHandler;
import org.clever.security.handler.UserLogoutSuccessHandler;
import org.clever.security.service.LoginUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import java.util.ArrayList;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-14 14:45 <br/>
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private UserLoginFilter userLoginFilter;
    @Autowired
    private LoginUserDetailsService loginUserDetailsService;
    @Autowired
    private UserLoginTokenAuthenticationProvider userLoginTokenAuthenticationProvider;
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

    public WebSecurityConfig() {
        super(false);
    }

    /**
     * 设置AuthenticationManager组件配置
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth
                // 添加认证组件
                .authenticationProvider(userLoginTokenAuthenticationProvider)
                // 设置认证事件发布组件
                // .authenticationEventPublisher(null)
                // 是否擦除认证凭证
                .eraseCredentials(true)
                // 设置获取用户认证信息和授权信息组件
                .userDetailsService(loginUserDetailsService)
                // 设置密码编码码器
                .passwordEncoder(bCryptPasswordEncoder);
    }

    /**
     * 在Spring容器中注册 AuthenticationManager
     */
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 使用自定义的 UserDetailsService
     */
    @Override
    protected UserDetailsService userDetailsService() {
        return loginUserDetailsService;
    }

    /**
     * 全局请求忽略规则配置（比如说静态文件，比如说注册页面）<br/>
     * 全局HttpFirewall配置 <br/>
     * 是否debug配置 <br/>
     * 全局SecurityFilterChain配置 <br/>
     * privilegeEvaluator、expressionHandler、securityInterceptor、 <br/>
     */
    @Override
    public void configure(WebSecurity web) {
        // 设置调试
        if (securityConfig.getEnableDebug() != null) {
            web.debug(securityConfig.getEnableDebug());
        }
        // 配置忽略的路径
        if (securityConfig.getIgnoreUrls() == null) {
            securityConfig.setIgnoreUrls(new ArrayList<>());
        }
        // 加入 /403.html
        if (StringUtils.isNotBlank(securityConfig.getForbiddenForwardPage())
                && !securityConfig.getIgnoreUrls().contains(securityConfig.getForbiddenForwardPage())) {
            securityConfig.getIgnoreUrls().add(securityConfig.getForbiddenForwardPage());
        }
        // 加入 /login.html
        if (StringUtils.isNotBlank(securityConfig.getLogin().getLoginPage())
                && !securityConfig.getIgnoreUrls().contains(securityConfig.getLogin().getLoginPage())) {
            securityConfig.getIgnoreUrls().add(securityConfig.getLogin().getLoginPage());
        }
        // 加入 login-failure-redirect-page
        if (StringUtils.isNotBlank(securityConfig.getLogin().getLoginFailureRedirectPage())
                && !securityConfig.getIgnoreUrls().contains(securityConfig.getLogin().getLoginFailureRedirectPage())) {
            securityConfig.getIgnoreUrls().add(securityConfig.getLogin().getLoginFailureRedirectPage());
        }
        // 加入 logout-success-redirect-page
        if (StringUtils.isNotBlank(securityConfig.getLogout().getLogoutSuccessRedirectPage())
                && !securityConfig.getIgnoreUrls().contains(securityConfig.getLogout().getLogoutSuccessRedirectPage())) {
            securityConfig.getIgnoreUrls().add(securityConfig.getLogout().getLogoutSuccessRedirectPage());
        }
        if (securityConfig.getIgnoreUrls().size() > 0) {
            web.ignoring().antMatchers(securityConfig.getIgnoreUrls().toArray(new String[0]));
            // 打印相应的日志
            if (log.isInfoEnabled()) {
                StringBuilder strTmp = new StringBuilder();
                strTmp.append("\r\n");
                strTmp.append("#=======================================================================================================================#\r\n");
                strTmp.append("不需要权限校验的资源配置:\r\n");
                for (String url : securityConfig.getIgnoreUrls()) {
                    strTmp.append("\t").append(url).append("\r\n");
                }
                strTmp.append("#=======================================================================================================================#");
                log.info(strTmp.toString());
            }
        }
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
        SecurityConfig.RememberMe rememberMe = securityConfig.getRememberMe();
        if (rememberMe != null && rememberMe.getEnable()) {
            http.rememberMe()
                    .rememberMeServices(rememberMeServices)
                    .alwaysRemember(rememberMe.getAlwaysRemember())
                    .tokenValiditySeconds(rememberMe.getValiditySeconds())
                    .rememberMeParameter(rememberMe.getRememberMeParameterName())
                    .rememberMeCookieName(UserLoginRememberMeServices.REMEMBER_ME)
                    .key(UserLoginRememberMeServices.REMEMBER_ME_KEY)
            ;
        }
        // 登录并发控制
        SecurityConfig.Login login = securityConfig.getLogin();
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