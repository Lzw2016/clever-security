package org.clever.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

/**
 * 作者： lzw<br/>
 * 创建时间：2017-12-04 10:37 <br/>
 */
@Configuration
@Slf4j
public class BeanConfiguration {
    
    /**
     * 密码处理
     */
    @Bean
    protected BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    }
}
