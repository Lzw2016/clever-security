package org.clever.security.session.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-18 12:21 <br/>
 */
@Configuration
@Slf4j
public class SecuritySessionBean {

    @Autowired
    private RedisOperationsSessionRepository sessionRepository;

    /**
     * 集成Spring Session所需的Bean
     */
    @Bean
    protected SpringSessionBackedSessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(this.sessionRepository);
    }

//    /**
//     * 集成Spring Session实现的记住我的功能
//     */
//    @Bean
//    protected RememberMeServices rememberMeServices() {
//        SecurityConfig.RememberMe rememberMe = securityConfig.getRememberMe();
//        if (rememberMe == null || rememberMe.getEnable() == null || !rememberMe.getEnable()) {
//            return new NullRememberMeServices();
//        }
//        SpringSessionRememberMeServices rememberMeServices = new UserLoginRememberMeServices();
//        rememberMeServices.setAlwaysRemember(rememberMe.getAlwaysRemember());
//        rememberMeServices.setRememberMeParameterName(rememberMe.getRememberMeParameterName());
//        rememberMeServices.setValiditySeconds(rememberMe.getValiditySeconds());
//        return rememberMeServices;
//    }
}
