package org.clever.security.repository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.security.config.SecurityConfig;
import org.clever.security.config.model.TokenConfig;
import org.clever.security.service.GenerateKeyService;
import org.clever.security.service.JWTTokenService;
import org.clever.security.token.JwtAccessToken;
import org.clever.security.token.JwtRefreshToken;
import org.clever.security.token.UserLoginToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-20 11:12 <br/>
 */
@Component
@Slf4j
public class RedisJwtRepository {

    /**
     * 刷新令牌有效时间
     */
    private final Duration refreshTokenValidity;

    @Autowired
    private JWTTokenService jwtTokenService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private GenerateKeyService generateKeyService;

    protected RedisJwtRepository(SecurityConfig securityConfig) {
        TokenConfig tokenConfig = securityConfig.getTokenConfig();
        if (tokenConfig == null || tokenConfig.getRefreshTokenValidity() == null) {
            throw new IllegalArgumentException("未配置TokenConfig");
        }
        refreshTokenValidity = tokenConfig.getRefreshTokenValidity();
    }

    // -------------------------------------------------------------------------------------------------------------------------------------- JwtAccessToken

    public JwtAccessToken saveJwtToken(UserLoginToken userLoginToken) {
        boolean rememberMe = userLoginToken.isRememberMe();
        // 保存 JwtAccessToken
        JwtAccessToken jwtAccessToken = jwtTokenService.createToken(userLoginToken, rememberMe);
        String jwtTokenKey = generateKeyService.getJwtTokenKey(jwtAccessToken.getClaims());
        if (jwtAccessToken.getClaims().getExpiration() == null) {
            redisTemplate.opsForValue().set(jwtTokenKey, jwtAccessToken);
        } else {
            long timeout = jwtAccessToken.getClaims().getExpiration().getTime() - System.currentTimeMillis();
            redisTemplate.opsForValue().set(jwtTokenKey, jwtAccessToken, timeout, TimeUnit.MILLISECONDS);
        }
        // 保存 JwtRefreshToken
        String jwtRefreshTokenKey = generateKeyService.getJwtRefreshTokenKey(jwtAccessToken.getRefreshToken());
        JwtRefreshToken jwtRefreshToken = new JwtRefreshToken(userLoginToken.getName(), jwtAccessToken.getClaims().getId());
        if (refreshTokenValidity.getSeconds() <= 0) {
            redisTemplate.opsForValue().set(jwtRefreshTokenKey, jwtRefreshToken);
        } else {
            redisTemplate.opsForValue().set(jwtRefreshTokenKey, jwtRefreshToken, refreshTokenValidity.getSeconds(), TimeUnit.SECONDS);
        }
        return jwtAccessToken;
    }

    public void deleteJwtTokenByKey(String jwtTokenKey) {
        JwtAccessToken jwtAccessToken = getJwtTokenByKey(jwtTokenKey);
        deleteJwtToken(jwtAccessToken);
    }

    public void deleteJwtToken(JwtAccessToken jwtAccessToken) {
        // 删除 JwtAccessToken
        String jwtTokenKey = generateKeyService.getJwtTokenKey(jwtAccessToken.getClaims());
        redisTemplate.delete(jwtTokenKey);
        // 删除 JwtRefreshToken
        String jwtRefreshTokenKey = generateKeyService.getJwtRefreshTokenKey(jwtAccessToken.getRefreshToken());
        redisTemplate.delete(jwtRefreshTokenKey);
    }

    public JwtAccessToken getJwtToken(HttpServletRequest request) {
        String token = request.getHeader(GenerateKeyService.JwtTokenHeaderKey);
        if (StringUtils.isBlank(token)) {
            throw new BusinessException("Token不存在");
        }
        return getJwtToken(token);
    }

    public JwtAccessToken getJwtToken(String token) {
        Jws<Claims> claimsJws = jwtTokenService.getClaimsJws(token);
        return getJwtToken(claimsJws.getBody());
    }

    public JwtAccessToken getJwtToken(Claims claims) {
        return getJwtToken(claims.getSubject(), claims.getId());
    }

    public JwtAccessToken getJwtToken(String username, String tokenId) {
        String jwtTokenKey = generateKeyService.getJwtTokenKey(username, tokenId);
        return getJwtTokenByKey(jwtTokenKey);
    }

    public JwtAccessToken getJwtTokenByKey(String jwtTokenKey) {
        Object object = redisTemplate.opsForValue().get(jwtTokenKey);
        if (object == null) {
            throw new BusinessException("JwtToken已过期");
        }
        if (!(object instanceof JwtAccessToken)) {
            throw new BusinessException("JwtToken类型错误");
        }
        return (JwtAccessToken) object;
    }

    public Set<String> getJwtTokenPatternKey(String username) {
        Set<String> ketSet = redisTemplate.keys(generateKeyService.getJwtTokenPatternKey(username));
        if (ketSet == null) {
            ketSet = new HashSet<>(0);
        }
        return ketSet;
    }

    // -------------------------------------------------------------------------------------------------------------------------------------- JwtRefreshToken

    public JwtRefreshToken getRefreshToken(String refreshToken) {
        String jwtRefreshTokenKey = generateKeyService.getJwtRefreshTokenKey(refreshToken);
        Object object = redisTemplate.opsForValue().get(jwtRefreshTokenKey);
        if (object == null) {
            throw new BusinessException("刷新令牌已过期");
        }
        if (!(object instanceof JwtRefreshToken)) {
            throw new BusinessException("刷新令牌类型错误");
        }
        return (JwtRefreshToken) object;
    }

    // -------------------------------------------------------------------------------------------------------------------------------------- SecurityContext

    public void saveSecurityContext(SecurityContext context) {
        String securityContextKey = generateKeyService.getSecurityContextKey(context.getAuthentication().getName());
        redisTemplate.opsForValue().set(securityContextKey, context);
    }

    public void deleteSecurityContext(String username) {
        String securityContextKey = generateKeyService.getSecurityContextKey(username);
        redisTemplate.delete(securityContextKey);
    }

    public SecurityContext getSecurityContext(JwtAccessToken jwtAccessToken) {
        return getSecurityContext(jwtAccessToken.getClaims().getSubject());
    }

    public SecurityContext getSecurityContext(Claims claims) {
        return getSecurityContext(claims.getSubject());
    }

    public SecurityContext getSecurityContext(String username) {
        String securityContextKey = generateKeyService.getSecurityContextKey(username);
        Object object = redisTemplate.opsForValue().get(securityContextKey);
        if (object == null) {
            throw new BusinessException("SecurityContext不存在");
        }
        if (!(object instanceof SecurityContext)) {
            throw new BusinessException("SecurityContext类型错误");
        }
        return (SecurityContext) object;
    }

    // -------------------------------------------------------------------------------------------------------------------------------------- validationToken

    /**
     * 验证令牌 (不会抛出异常)
     */
    public boolean validationToken(HttpServletRequest request) {
        String token = request.getHeader(GenerateKeyService.JwtTokenHeaderKey);
        if (StringUtils.isBlank(token)) {
            return false;
        }
        return validationToken(token);
    }

    /**
     * 验证令牌 (不会抛出异常)
     */
    public boolean validationToken(String token) {
        try {
            Jws<Claims> claimsJws = jwtTokenService.getClaimsJws(token);
            if (claimsJws != null) {
                return true;
            }
        } catch (Throwable ignored) {
        }
        return false;
    }
}
