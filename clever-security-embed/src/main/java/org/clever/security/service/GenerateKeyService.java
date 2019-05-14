package org.clever.security.service;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.security.config.SecurityConfig;
import org.clever.security.config.model.TokenConfig;
import org.springframework.stereotype.Component;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-19 11:22 <br/>
 */
@Component
@Slf4j
public class GenerateKeyService {
    /**
     * SecurityContext Key
     */
    private static final String SecurityContextKey = "security-context";
    /**
     * JWT Token 令牌Key
     */
    private static final String JwtTokenKey = "jwt-token";
    /**
     * JWT Token 刷新令牌Key
     */
    private static final String JwtTokenRefreshKey = "refresh-token";
    /**
     * 验证码 Key
     */
    private static final String CaptchaInfoKey = "captcha-info";
    /**
     * 登录失败次数 Key
     */
    private static final String LoginFailCountKey = "login-fail-count";

    /**
     * Token Redis前缀
     */
    private final String redisNamespace;

    protected GenerateKeyService(SecurityConfig securityConfig) {
        TokenConfig tokenConfig = securityConfig.getTokenConfig();
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
        return getJwtTokenKey(claims.getSubject(), claims.getId());
    }

    /**
     * 生成 Redis 存储 JwtTokenKey
     */
    public String getJwtTokenKey(String username, String tokenId) {
        // {redisNamespace}:{JwtTokenKey}:{username}:{JWT ID}
        return String.format("%s:%s:%s:%s", redisNamespace, JwtTokenKey, username, tokenId);
    }

    /**
     * 生成 Redis 存储 JwtTokenKey Pattern
     */
    public String getJwtTokenPatternKey(String username) {
        // {redisNamespace}:{JwtTokenKey}:{username}:{*}
        return String.format("%s:%s:%s:%s", redisNamespace, JwtTokenKey, username, "*");
    }

    /**
     * 生成 Redis 存储 JwtTokenRefreshKey
     */
    public String getJwtRefreshTokenKey(String refreshToken) {
        // {redisNamespace}:{JwtTokenRefreshKey}:{refreshToken}
        return String.format("%s:%s:%s", redisNamespace, JwtTokenRefreshKey, refreshToken);
    }

    /**
     * 生成 Redis 存储 CaptchaInfoKey
     */
    public String getCaptchaInfoKey(String code, String imageDigest) {
        // {redisNamespace}:{CaptchaInfoKey}:{code}:{imageDigest}
        return String.format("%s:%s:%s:%s", redisNamespace, CaptchaInfoKey, code, imageDigest);
    }

    /**
     * 生成 Redis 存储 LoginFailCountKey
     */
    public String getLoginFailCountKey(String username) {
        // {redisNamespace}:{LoginFailCountKey}:{username}
        return String.format("%s:%s:%s", redisNamespace, LoginFailCountKey, username);
    }

//    /**
//     * 生成 Redis 存储 JwtTokenRefreshKey Pattern
//     */
//    public String getJwtRefreshTokenPatternKey(String username) {
//        // {redisNamespace}:{JwtTokenRefreshKey}:{username}:{*}
//        return String.format("%s:%s:%s:%s", redisNamespace, JwtTokenRefreshKey, username, "*");
//    }
}
