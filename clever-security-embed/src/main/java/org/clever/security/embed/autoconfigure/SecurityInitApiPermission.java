package org.clever.security.embed.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.embed.config.SecurityConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

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
    protected boolean initialized = false;

    public SecurityInitApiPermission(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

    @Override
    public synchronized void run(String... args) {
        if (initialized) {
            return;
        }
        initialized = true;
    }
}
