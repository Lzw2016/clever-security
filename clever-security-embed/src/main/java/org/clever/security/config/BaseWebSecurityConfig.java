package org.clever.security.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-04-26 10:06 <br/>
 */
@Slf4j
public abstract class BaseWebSecurityConfig extends WebSecurityConfigurerAdapter {

    public BaseWebSecurityConfig() {
        super(false);
    }

    /**
     * 密码编码码器
     */
    abstract PasswordEncoder getPasswordEncoder();

    /**
     * 获取用户信息组件
     */
    abstract UserDetailsService getUserDetailsService();

    /**
     * 用户认证组件
     */
    abstract List<AuthenticationProvider> getAuthenticationProviderList();

    /**
     * 配置信息
     */
    abstract SecurityConfig getSecurityConfig();

    /**
     * 设置AuthenticationManager组件配置
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        // 添加认证组件
        List<AuthenticationProvider> authenticationProviderList = getAuthenticationProviderList();
        if (authenticationProviderList == null || authenticationProviderList.size() <= 0) {
            throw new RuntimeException("缺少认证组件");
        }
        for (AuthenticationProvider authenticationProvider : authenticationProviderList) {
            auth.authenticationProvider(authenticationProvider);
        }
        auth
                // 设置认证事件发布组件
                // .authenticationEventPublisher(null)
                // 是否擦除认证凭证
                .eraseCredentials(true)
                // 设置获取用户认证信息和授权信息组件
                .userDetailsService(getUserDetailsService())
                // 设置密码编码码器
                .passwordEncoder(getPasswordEncoder());
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
        return getUserDetailsService();
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
        SecurityConfig securityConfig = getSecurityConfig();
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
                strTmp.append("不需要登录认证的资源:\r\n");
                for (String url : securityConfig.getIgnoreUrls()) {
                    strTmp.append("\t").append(url).append("\r\n");
                }
                strTmp.append("#=======================================================================================================================#");
                log.info(strTmp.toString());
            }
        }
    }
}
