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
import org.clever.security.rememberme.RememberMeRepository;
import org.clever.security.rememberme.RememberMeServices;
import org.clever.security.rememberme.RememberMeUserDetailsChecker;
import org.clever.security.service.GlobalUserDetailsService;
import org.clever.security.strategy.SessionExpiredStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
    @ConditionalOnMissingBean(BCryptPasswordEncoder.class)
    @Bean
    protected BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    }

    /**
     * 使用 GenericJackson2JsonRedisSerializer 替换默认序列化
     */
    // @ConditionalOnMissingBean(RedisTemplate.class)
    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 自定义 ObjectMapper
        ObjectMapper objectMapper = newObjectMapper();
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
     * 授权校验 <br/>
     * <pre>
     * {@code
     *   AffirmativeBased (有一个允许访问就有权访问)
     *     1. 只要有AccessDecisionVoter的投票为ACCESS_GRANTED则同意用户进行访问
     *     2. 如果全部弃权也表示通过
     *     3. 如果没有一个人投赞成票，但是有人投反对票，则将抛出AccessDeniedException
     *   ConsensusBased (大多数允许访问才有权访问)
     *     1. 如果赞成票多于反对票则表示通过
     *     2. 反过来，如果反对票多于赞成票则将抛出AccessDeniedException
     *     3. 如果赞成票与反对票相同且不等于0，并且属性allowIfEqualGrantedDeniedDecisions的值为true，则表示通过，否则将抛出异常AccessDeniedException。参数allowIfEqualGrantedDeniedDecisions的值默认为true
     *     4. 如果所有的AccessDecisionVoter都弃权了，则将视参数allowIfAllAbstainDecisions的值而定，如果该值为true则表示通过，否则将抛出异常AccessDeniedException。参数allowIfAllAbstainDecisions的值默认为false
     *   UnanimousBased
     *     逻辑与另外两种实现有点不一样，
     *     另外两种会一次性把受保护对象的配置属性全部传递给AccessDecisionVoter进行投票，
     *     而UnanimousBased会一次只传递一个ConfigAttribute给AccessDecisionVoter进行投票。
     *     这也就意味着如果我们的AccessDecisionVoter的逻辑是只要传递进来的ConfigAttribute中有一个能够匹配则投赞成票，
     *     但是放到UnanimousBased中其投票结果就不一定是赞成了
     *     1. 如果受保护对象配置的某一个ConfigAttribute被任意的AccessDecisionVoter反对了，则将抛出AccessDeniedException
     *     2. 如果没有反对票，但是有赞成票，则表示通过
     *     3. 如果全部弃权了，则将视参数allowIfAllAbstainDecisions的值而定，true则通过，false则抛出AccessDeniedException
     * }
     * </pre>
     */
    @Bean
    protected AccessDecisionManager accessDecisionManager(@Autowired RequestAccessDecisionVoter requestAccessDecisionVoter) {
        // WebExpressionVoter RoleVoter AuthenticatedVoter
        List<AccessDecisionVoter<?>> decisionVoters = Collections.singletonList(requestAccessDecisionVoter);
        // AffirmativeBased accessDecisionManager = new AffirmativeBased(decisionVoters);
        // accessDecisionManager.
        // TODO 支持注解权限校验
        return new AffirmativeBased(decisionVoters);
    }

    /**
     * 登录并发处理(使用session登录时才需要)
     */
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "login-model", havingValue = "session")
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
     * Session过期处理(使用session登录时才需要)
     */
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "login-model", havingValue = "session")
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
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "login-model", havingValue = "session")
    @Bean
    protected org.springframework.security.web.authentication.RememberMeServices rememberMeServices(
            @Autowired RememberMeUserDetailsChecker rememberMeUserDetailsChecker,
            @Autowired GlobalUserDetailsService userDetailsService,
            @Autowired RememberMeRepository rememberMeRepository) {
        RememberMeConfig rememberMe = securityConfig.getRememberMe();
        if (rememberMe == null || rememberMe.getEnable() == null || !rememberMe.getEnable()) {
            return new NullRememberMeServices();
        }
        RememberMeServices rememberMeServices = new RememberMeServices(
                RememberMeServices.REMEMBER_ME_KEY,
                userDetailsService,
                rememberMeRepository,
                rememberMeUserDetailsChecker);
        rememberMeServices.setAlwaysRemember(rememberMe.getAlwaysRemember());
        rememberMeServices.setParameter(CollectLoginToken.REMEMBER_ME_PARAM);
        rememberMeServices.setTokenValiditySeconds((int) rememberMe.getValidity().getSeconds());
        rememberMeServices.setCookieName(RememberMeServices.REMEMBER_ME_COOKIE_NAME);
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
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "login-model", havingValue = "session")
    @Bean("springSessionDefaultRedisSerializer")
    protected RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        //解决查询缓存转换异常的问题
        ObjectMapper objectMapper = newObjectMapper();
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }

    /**
     * 监听 HttpSession 事件
     */
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "login-model", havingValue = "session")
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    /**
     * 集成Spring Session所需的Bean
     */
    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "login-model", havingValue = "session")
    @Bean
    protected SpringSessionBackedSessionRegistry sessionRegistry(@Autowired RedisOperationsSessionRepository sessionRepository) {
        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
    }

    private ObjectMapper newObjectMapper() {
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
        return objectMapper;
    }
}
