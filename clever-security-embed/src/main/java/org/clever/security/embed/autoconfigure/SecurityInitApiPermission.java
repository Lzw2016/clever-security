package org.clever.security.embed.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.dto.request.GetDomainReq;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.entity.Domain;
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
    private final LoginSupportClient loginSupportClient;
    protected boolean initialized = false;

    public SecurityInitApiPermission(
            SecurityConfig securityConfig,
            ObjectProvider<List<ObjectMapper>> objectMapperList,
            LoginSupportClient loginSupportClient) {
        this.securityConfig = securityConfig;
        this.objectMapperList = objectMapperList.getIfAvailable();
        this.loginSupportClient = loginSupportClient;
    }

    @Override
    public synchronized void run(String... args) {
        if (initialized) {
            return;
        }
        initialized = true;
        // 初始化 ObjectMapper
        JacksonMapper.getInstance().getMapper().registerModule(CleverSecurityJackson2Module.instance);
        if (objectMapperList != null) {
            for (ObjectMapper objectMapper : objectMapperList) {
                if (objectMapper == JacksonMapper.getInstance().getMapper()) {
                    continue;
                }
                objectMapper.registerModule(CleverSecurityJackson2Module.instance);
            }
        }
        // 打印当前服务所在域
        Domain domain = loginSupportClient.getDomain(new GetDomainReq(securityConfig.getDomainId()));
        if (domain == null) {
            log.error("服务启动失败", new IllegalArgumentException(String.format("配置security.domain-id=%s错误,domain不存在", securityConfig.getDomainId())));
            System.exit(-1);
        }
        log.info("### 当前系统domain信息 | domain-id={} | name={} | redis-name-space={}", domain.getId(), domain.getName(), domain.getRedisNameSpace());
        // TODO 初始化API权限信息
    }
}
