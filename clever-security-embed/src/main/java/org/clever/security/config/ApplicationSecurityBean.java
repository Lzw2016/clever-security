package org.clever.security.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.security.Constant;
import org.clever.security.authentication.CollectLoginToken;
import org.clever.security.authorization.RequestAccessDecisionVoter;
import org.clever.security.config.model.LoginConfig;
import org.clever.security.config.model.RememberMeConfig;
import org.clever.security.jackson2.CleverSecurityJackson2Module;
import org.clever.security.rememberme.RememberMeUserDetailsChecker;
import org.clever.security.rememberme.UserLoginRememberMeServices;
import org.clever.security.rememberme.UserLoginTokenRepository;
import org.clever.security.service.GlobalUserDetailsService;
import org.clever.security.strategy.SessionExpiredStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-17 9:49 <br/>
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@Slf4j
public class ApplicationSecurityBean {

    @Autowired
    private SecurityConfig securityConfig;

    /**
     * 密码处理
     */
    @Bean
    protected BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    }

    /**
     * 使用 GenericJackson2JsonRedisSerializer 替换默认序列化
     */
    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 自定义 ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
//        objectMapper.setDateFormat();
//        objectMapper.setDefaultMergeable()
//        objectMapper.setDefaultPropertyInclusion()
//        objectMapper.setDefaultSetterInfo()
//        objectMapper.setDefaultVisibility()
        // 查看Spring的实现 SecurityJackson2Modules
        List<Module> modules = SecurityJackson2Modules.getModules(getClass().getClassLoader());
        objectMapper.findAndRegisterModules();
        objectMapper.registerModules(modules);
//        objectMapper.registerModule(new org.clever.security.session.jackson2.CleverSecurityJackson2Module());
//        objectMapper.registerModule(new org.clever.security.jwt.jackson2.CleverSecurityJackson2Module());
        // 创建 RedisTemplate
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        // 设置value的序列化规则和 key的序列化规则
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 登录并发处理(使用session登录时才需要)
     */
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "loginModel", havingValue = "session")
    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy(@Autowired SessionRegistry sessionRegistry) {
        LoginConfig login = securityConfig.getLogin();
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
     * 授权校验(使用session登录时才需要) TODO ？？jwt 也要
     */
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "loginModel", havingValue = "session")
    @Bean
    protected AccessDecisionManager accessDecisionManager(@Autowired RequestAccessDecisionVoter requestAccessDecisionVoter) {
        // WebExpressionVoter RoleVoter AuthenticatedVoter
        List<AccessDecisionVoter<?>> decisionVoters = Collections.singletonList(requestAccessDecisionVoter);
        // AccessDecisionManager accessDecisionManager = new AffirmativeBased(decisionVoters);
        // accessDecisionManager.
        // TODO 支持注解权限校验
        return new AffirmativeBased(decisionVoters);
    }

    /**
     * Session过期处理(使用session登录时才需要)
     */
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "loginModel", havingValue = "session")
    @Bean
    protected SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        SessionExpiredStrategy sessionExpiredStrategy = new SessionExpiredStrategy();
        sessionExpiredStrategy.setNeedRedirect(StringUtils.isNotBlank(securityConfig.getSessionExpiredRedirectUrl()));
        sessionExpiredStrategy.setDestinationUrl(securityConfig.getSessionExpiredRedirectUrl());
        return sessionExpiredStrategy;
    }

    /**
     * 实现的记住我的功能,不使用SpringSession提供的SpringSessionRememberMeServices(使用session登录时才需要)
     */
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "loginModel", havingValue = "session")
    @Bean
    protected RememberMeServices rememberMeServices(
            @Autowired RememberMeUserDetailsChecker rememberMeUserDetailsChecker,
            @Autowired GlobalUserDetailsService userDetailsService,
            @Autowired UserLoginTokenRepository userLoginTokenRepository) {
        RememberMeConfig rememberMe = securityConfig.getRememberMe();
        if (rememberMe == null || rememberMe.getEnable() == null || !rememberMe.getEnable()) {
            return new NullRememberMeServices();
        }
        UserLoginRememberMeServices rememberMeServices = new UserLoginRememberMeServices(
                UserLoginRememberMeServices.REMEMBER_ME_KEY,
                userDetailsService,
                userLoginTokenRepository,
                rememberMeUserDetailsChecker);
        rememberMeServices.setAlwaysRemember(rememberMe.getAlwaysRemember());
        rememberMeServices.setParameter(CollectLoginToken.REMEMBER_ME_PARAM);
        rememberMeServices.setTokenValiditySeconds((int) rememberMe.getValidity().getSeconds());
        rememberMeServices.setCookieName(UserLoginRememberMeServices.REMEMBER_ME_COOKIE_NAME);
//        rememberMeServices.setTokenLength();
//        rememberMeServices.setSeriesLength();
//        rememberMeServices.setUserDetailsChecker();
//        rememberMeServices.setUseSecureCookie();
//        rememberMeServices.setCookieDomain();
        return rememberMeServices;
    }

    /**
     * 替换Spring Session Redis默认的序列化方式 JdkSerializationRedisSerializer<br />
     *
     * @see org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration#setDefaultRedisSerializer
     */
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "loginModel", havingValue = "session")
    @Bean("springSessionDefaultRedisSerializer")
    protected RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        //解决查询缓存转换异常的问题
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
//        objectMapper.setDateFormat();
//        objectMapper.setDefaultMergeable()
//        objectMapper.setDefaultPropertyInclusion()
//        objectMapper.setDefaultSetterInfo()
//        objectMapper.setDefaultVisibility()
        // 查看Spring的实现 SecurityJackson2Modules
        List<Module> modules = SecurityJackson2Modules.getModules(getClass().getClassLoader());
        objectMapper.findAndRegisterModules();
        objectMapper.registerModules(modules);
        objectMapper.registerModule(new CleverSecurityJackson2Module());
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }

    /**
     * 监听 HttpSession 事件
     */
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "loginModel", havingValue = "session")
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    /**
     * 集成Spring Session所需的Bean
     */
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "loginModel", havingValue = "session")
    @Bean
    protected SpringSessionBackedSessionRegistry sessionRegistry(@Autowired RedisOperationsSessionRepository sessionRepository) {
        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
    }
}
