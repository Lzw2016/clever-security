package org.clever.security.embed.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.jackson2.CleverSecurityJackson2Module;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/20 14:06 <br/>
 */
@Order
@Configuration
@EnableConfigurationProperties({SecurityConfig.class})
@Slf4j
public class SecurityInitApiPermission implements CommandLineRunner {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    private final List<ObjectMapper> objectMapperList;
    protected boolean initialized = false;

    public SecurityInitApiPermission(SecurityConfig securityConfig, ObjectProvider<List<ObjectMapper>> objectMapperList) {
        this.securityConfig = securityConfig;
        this.objectMapperList = objectMapperList.getIfAvailable();
    }

    @Override
    public synchronized void run(String... args) {
        if (initialized) {
            return;
        }
        initialized = true;
        JacksonMapper.getInstance().getMapper().registerModule(CleverSecurityJackson2Module.instance);
        if (objectMapperList != null) {
            for (ObjectMapper objectMapper : objectMapperList) {
                objectMapper.registerModule(CleverSecurityJackson2Module.instance);
            }
        }
    }
}
