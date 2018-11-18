package org.clever.security.jwt.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.security.jwt.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-18 17:27 <br/>
 */
@Component
@Slf4j
public class JwtRedisSecurityContextRepository implements SecurityContextRepository {

    /**
     * Token Redis前缀
     */
    private final String redisNamespace;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    protected JwtRedisSecurityContextRepository(SecurityConfig securityConfig) {
        SecurityConfig.TokenConfig tokenConfig = securityConfig.getTokenConfig();
        if (tokenConfig == null || StringUtils.isBlank(tokenConfig.getRedisNamespace())) {
            throw new BusinessException("TokenConfig 的 RedisNamespace 属性未配置");
        }
        redisNamespace = tokenConfig.getRedisNamespace();
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {


        return null;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return false;
    }
}
