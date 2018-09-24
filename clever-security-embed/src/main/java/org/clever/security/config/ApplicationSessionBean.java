package org.clever.security.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.clever.security.jackson2.CleverSecurityJackson2Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-18 10:42 <br/>
 */
@Configuration
@Slf4j
public class ApplicationSessionBean {

    /**
     * 替换Spring Session Redis默认的序列化方式 JdkSerializationRedisSerializer<br />
     *
     * @see org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration#defaultRedisSerializer
     */
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
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
