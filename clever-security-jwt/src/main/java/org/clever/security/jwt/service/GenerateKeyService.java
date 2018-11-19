package org.clever.security.jwt.service;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.security.jwt.config.SecurityConfig;
import org.springframework.stereotype.Component;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-19 11:22 <br/>
 */
@Component
@Slf4j
public class GenerateKeyService {
    /**
     * JWT Token请求头Key
     */
    public static final String JwtTokenHeaderKey = "Authorization";

    /**
     * JWT Token 刷新令牌Key
     */
    public static final String JwtTokenRefreshKey = "refreshToken";

    private static final String SecurityContextKey = "security-context";
    private static final String JwtTokenKey = "jwt-token";

    /**
     * Token Redis前缀
     */
    private final String redisNamespace;

    protected GenerateKeyService(SecurityConfig securityConfig) {
        SecurityConfig.TokenConfig tokenConfig = securityConfig.getTokenConfig();
        if (tokenConfig == null || StringUtils.isBlank(tokenConfig.getRedisNamespace())) {
            throw new BusinessException("TokenConfig 的 RedisNamespace 属性未配置");
        }
        String tmp = tokenConfig.getRedisNamespace();
        if (tmp.endsWith(":")) {
            tmp = tmp.substring(0, tmp.length() - 1);
        }
        redisNamespace = tmp;
    }

    /**
     * 生成 Redis 存储 SecurityContextKey
     */
    public String getSecurityContextKey(String username) {
        // {redisNamespace}:{SecurityContextKey}:{username}
        return String.format("%s:%s:%s", redisNamespace, SecurityContextKey, username);
    }

    /**
     * 生成 Redis 存储 JwtTokenKey
     */
    public String getJwtTokenKey(Claims claims) {
        // {redisNamespace}:{JwtTokenKey}:{username}:{JWT ID}
        return String.format("%s:%s:%s:%s", redisNamespace, JwtTokenKey, claims.getSubject(), claims.getId());
    }

    /**
     * 生成 Redis 存储 JwtTokenKey Pattern
     */
    public String getJwtTokenPatternKey(String username) {
        // {redisNamespace}:{JwtTokenKey}:{username}:{*}
        return String.format("%s:%s:%s:%s", redisNamespace, JwtTokenKey, username, "*");
    }
}
