package org.clever.security.jwt.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.clever.security.jwt.jackson2.CleverSecurityJackson2Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.jackson2.SecurityJackson2Modules;

import java.security.SecureRandom;
import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-17 9:49 <br/>
 */
//@EnableFeignClients(basePackages = {"org.clever.security.client"})
@SuppressWarnings("Duplicates")
@Configuration
@Slf4j
public class ApplicationSecurityBean {

    @Autowired
    private SecurityConfig securityConfig;
//    @Autowired
//    private RequestAccessDecisionVoter requestAccessDecisionVoter;
//    @Autowired
//    private SessionRegistry sessionRegistry;
//    @Autowired
//    private RememberMeUserDetailsChecker rememberMeUserDetailsChecker;

//    /**
//     * 授权校验
//     */
//    @Bean
//    protected AccessDecisionManager accessDecisionManager() {
//        // WebExpressionVoter RoleVoter AuthenticatedVoter
//        List<AccessDecisionVoter<?>> decisionVoters = Collections.singletonList(requestAccessDecisionVoter);
//        AccessDecisionManager accessDecisionManager = new AffirmativeBased(decisionVoters);
//        // accessDecisionManager.
//        // TODO 支持注解权限校验
//        return accessDecisionManager;
//    }

    /**
     * 密码处理
     */
    @Bean
    protected BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    }

//    /**
//     * 登录并发处理
//     */
//    @Bean
//    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
//        SecurityConfig.Login login = securityConfig.getLogin();
//        if (login.getConcurrentLoginCount() == null) {
//            return new NullAuthenticatedSessionStrategy();
//        }
//        int concurrentLoginCount = login.getConcurrentLoginCount() <= 0 ? -1 : login.getConcurrentLoginCount();
//        boolean notAllowAfterLogin = false;
//        if (login.getNotAllowAfterLogin() != null) {
//            notAllowAfterLogin = login.getNotAllowAfterLogin();
//        }
//        ConcurrentSessionControlAuthenticationStrategy sessionAuthenticationStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry);
//        sessionAuthenticationStrategy.setMaximumSessions(concurrentLoginCount);
//        sessionAuthenticationStrategy.setExceptionIfMaximumExceeded(notAllowAfterLogin);
//        // sessionAuthenticationStrategy.setMessageSource();
//        return sessionAuthenticationStrategy;
//    }

//    /**
//     * Session 过期处理
//     */
//    @Bean
//    protected SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
//        SessionExpiredStrategy sessionExpiredStrategy = new SessionExpiredStrategy();
//        sessionExpiredStrategy.setNeedRedirect(StringUtils.isNotBlank(securityConfig.getSessionExpiredRedirectUrl()));
//        sessionExpiredStrategy.setDestinationUrl(securityConfig.getSessionExpiredRedirectUrl());
//        return sessionExpiredStrategy;
//    }

//    /**
//     * 实现的记住我的功能, 不使用 Spring Session 提供的 SpringSessionRememberMeServices
//     */
//    @Bean
//    protected RememberMeServices rememberMeServices(LoginUserDetailsService userDetailsService, UserLoginTokenRepository userLoginTokenRepository) {
//        SecurityConfig.RememberMe rememberMe = securityConfig.getRememberMe();
//        if (rememberMe == null || rememberMe.getEnable() == null || !rememberMe.getEnable()) {
//            return new NullRememberMeServices();
//        }
//        UserLoginRememberMeServices rememberMeServices = new UserLoginRememberMeServices(
//                UserLoginRememberMeServices.REMEMBER_ME_KEY,
//                userDetailsService,
//                userLoginTokenRepository,
//                rememberMeUserDetailsChecker);
//        rememberMeServices.setAlwaysRemember(rememberMe.getAlwaysRemember());
//        rememberMeServices.setParameter(rememberMe.getRememberMeParameterName());
//        rememberMeServices.setTokenValiditySeconds(rememberMe.getValiditySeconds());
//        rememberMeServices.setCookieName(UserLoginRememberMeServices.REMEMBER_ME);
////        rememberMeServices.setTokenLength();
////        rememberMeServices.setSeriesLength();
////        rememberMeServices.setUserDetailsChecker();
////        rememberMeServices.setUseSecureCookie();
////        rememberMeServices.setCookieDomain();
//        return rememberMeServices;
//    }

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
        objectMapper.registerModule(new CleverSecurityJackson2Module());
        // 创建 RedisTemplate
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        // 设置value的序列化规则和 key的序列化规则
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        template.afterPropertiesSet();
        return template;
    }
}
