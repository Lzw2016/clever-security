package org.clever.security.embed.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.embed.config.SecurityConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 12:39 <br/>
 */
@Configuration
@EnableConfigurationProperties({SecurityConfig.class})
@Slf4j
public class AutoConfigureSecurity {
    private final SecurityConfig securityConfig;

    public AutoConfigureSecurity(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

    /**
     * 密码处理
     */
    @ConditionalOnMissingBean(BCryptPasswordEncoder.class)
    @Bean
    protected BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    }
}
