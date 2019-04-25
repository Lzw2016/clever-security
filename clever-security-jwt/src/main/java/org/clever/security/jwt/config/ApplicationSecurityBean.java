package org.clever.security.jwt.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.clever.security.jwt.jackson2.CleverSecurityJackson2Module;
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
@Configuration
@Slf4j
public class ApplicationSecurityBean {

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
