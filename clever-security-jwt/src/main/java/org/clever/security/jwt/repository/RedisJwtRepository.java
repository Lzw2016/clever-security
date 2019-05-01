package org.clever.security.jwt.repository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.security.jwt.config.SecurityConfig;
import org.clever.security.jwt.model.JwtToken;
import org.clever.security.jwt.model.RefreshToken;
import org.clever.security.jwt.model.UserLoginToken;
import org.clever.security.jwt.service.GenerateKeyService;
import org.clever.security.jwt.service.JwtTokenService;
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
@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "unused"})
@Component
@Slf4j
public class RedisJwtRepository {

    /**
     * 刷新令牌有效时间
     */
    private final Duration refreshTokenValidity;

    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private GenerateKeyService generateKeyService;

    protected RedisJwtRepository(SecurityConfig securityConfig) {
        SecurityConfig.TokenConfig tokenConfig = securityConfig.getTokenConfig();
        if (tokenConfig == null || tokenConfig.getRefreshTokenValidity() == null) {
            throw new IllegalArgumentException("未配置TokenConfig");
        }
        refreshTokenValidity = tokenConfig.getRefreshTokenValidity();
    }

    // -------------------------------------------------------------------------------------------------------------------------------------- JwtToken

    public JwtToken saveJwtToken(UserLoginToken userLoginToken) {
        boolean rememberMe = false;
        if (StringUtils.isNotBlank(userLoginToken.getRememberMe())) {
            if (userLoginToken.getRememberMe().equalsIgnoreCase("true")
                    || userLoginToken.getRememberMe().equalsIgnoreCase("on")
                    || userLoginToken.getRememberMe().equalsIgnoreCase("yes")
                    || userLoginToken.getRememberMe().equals("1")) {
                rememberMe = true;
            }
        }
        // 保存 JwtToken
        JwtToken JwtToken = jwtTokenService.createToken(userLoginToken, rememberMe);
        String JwtTokenKey = generateKeyService.getJwtTokenKey(JwtToken.getClaims());
        if (JwtToken.getClaims().getExpiration() == null) {
            redisTemplate.opsForValue().set(JwtTokenKey, JwtToken);
        } else {
            long timeout = JwtToken.getClaims().getExpiration().getTime() - System.currentTimeMillis();
            redisTemplate.opsForValue().set(JwtTokenKey, JwtToken, timeout, TimeUnit.MILLISECONDS);
        }
        // 保存 RefreshToken
        String jwtRefreshTokenKey = generateKeyService.getJwtRefreshTokenKey(JwtToken.getRefreshToken());
        RefreshToken refreshToken = new RefreshToken(userLoginToken.getName(), JwtToken.getClaims().getId());
        if (refreshTokenValidity.getSeconds() <= 0) {
            redisTemplate.opsForValue().set(jwtRefreshTokenKey, refreshToken);
        } else {
            redisTemplate.opsForValue().set(jwtRefreshTokenKey, refreshToken, refreshTokenValidity.getSeconds(), TimeUnit.SECONDS);
        }
        return JwtToken;
    }

    public void deleteJwtTokenByKey(String JwtTokenKey) {
        JwtToken JwtToken = getJwtTokenByKey(JwtTokenKey);
        deleteJwtToken(JwtToken);
    }

    public void deleteJwtToken(JwtToken JwtToken) {
        // 删除 JwtToken
        String JwtTokenKey = generateKeyService.getJwtTokenKey(JwtToken.getClaims());
        redisTemplate.delete(JwtTokenKey);
        // 删除 RefreshToken
        String jwtRefreshTokenKey = generateKeyService.getJwtRefreshTokenKey(JwtToken.getRefreshToken());
        redisTemplate.delete(jwtRefreshTokenKey);
    }

    public JwtToken getJwtToken(HttpServletRequest request) {
        String token = request.getHeader(GenerateKeyService.JwtTokenHeaderKey);
        if (StringUtils.isBlank(token)) {
            throw new BusinessException("Token不存在");
        }
        return getJwtToken(token);
    }

    public JwtToken getJwtToken(String token) {
        Jws<Claims> claimsJws = jwtTokenService.getClaimsJws(token);
        return getJwtToken(claimsJws.getBody());
    }

    public JwtToken getJwtToken(Claims claims) {
        return getJwtToken(claims.getSubject(), claims.getId());
    }

    public JwtToken getJwtToken(String username, String tokenId) {
        String JwtTokenKey = generateKeyService.getJwtTokenKey(username, tokenId);
        return getJwtTokenByKey(JwtTokenKey);
    }

    public JwtToken getJwtTokenByKey(String JwtTokenKey) {
        Object object = redisTemplate.opsForValue().get(JwtTokenKey);
        if (object == null) {
            throw new BusinessException("JwtToken已过期");
        }
        if (!(object instanceof JwtToken)) {
            throw new BusinessException("JwtToken类型错误");
        }
        return (JwtToken) object;
    }

    public Set<String> getJwtTokenPatternKey(String username) {
        Set<String> ketSet = redisTemplate.keys(generateKeyService.getJwtTokenPatternKey(username));
        if (ketSet == null) {
            ketSet = new HashSet<>(0);
        }
        return ketSet;
    }

    // -------------------------------------------------------------------------------------------------------------------------------------- RefreshToken

    public RefreshToken getRefreshToken(String refreshToken) {
        String jwtRefreshTokenKey = generateKeyService.getJwtRefreshTokenKey(refreshToken);
        Object object = redisTemplate.opsForValue().get(jwtRefreshTokenKey);
        if (object == null) {
            throw new BusinessException("刷新令牌已过期");
        }
        if (!(object instanceof RefreshToken)) {
            throw new BusinessException("刷新令牌类型错误");
        }
        return (RefreshToken) object;
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

    public SecurityContext getSecurityContext(JwtToken JwtToken) {
        return getSecurityContext(JwtToken.getClaims().getSubject());
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
