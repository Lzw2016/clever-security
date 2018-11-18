package org.clever.security.jwt.repository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.SnowFlake;
import org.clever.security.jwt.config.SecurityConfig;
import org.clever.security.jwt.model.JwtToken;
import org.clever.security.jwt.service.JWTTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-18 17:27 <br/>
 */
@Component
@Slf4j
public class JwtRedisSecurityContextRepository implements SecurityContextRepository {

    private static final String SecurityContextKey = "security-context";
    private static final String JwtTokenKey = "jwt-token";

    /**
     * Token Redis前缀
     */
    private final String redisNamespace;

    //    private final Object contextObject = SecurityContextHolder.createEmptyContext();
//    private boolean disableUrlRewriting = false;
    private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    @Autowired
    private JWTTokenService jwtTokenService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    protected JwtRedisSecurityContextRepository(SecurityConfig securityConfig) {
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
     * 从Redis读取Jwt Token
     */
    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
//        HttpServletResponse response = requestResponseHolder.getResponse();
        String jwtToken = request.getHeader("Authorization");
        if (StringUtils.isBlank(jwtToken)) {
            return SecurityContextHolder.createEmptyContext();
        }
        Jws<Claims> claimsJws = jwtTokenService.getClaimsJws(jwtToken);
        if (claimsJws == null) {
            return SecurityContextHolder.createEmptyContext();
        }
        // 保存 context
        String securityContextKey = getSecurityContextKey(claimsJws.getBody().getSubject());
        Object object = redisTemplate.opsForValue().get(securityContextKey);
        if (!(object instanceof SecurityContext)) {
            return SecurityContextHolder.createEmptyContext();
        }
        return (SecurityContext) object;
    }

    /**
     * 保存Jwt Token到Redis
     */
    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        final Authentication authentication = context.getAuthentication();
        // 认证信息不存在或者是匿名认证不保存
        if (authentication == null || trustResolver.isAnonymous(authentication)) {
            return;
        }
        // 保存 context
        String securityContextKey = getSecurityContextKey(authentication.getName());
        redisTemplate.opsForValue().set(securityContextKey, context);
        // 保存 JWT Token
        JwtToken jwtToken = jwtTokenService.createToken(authentication, false);
        String jwtTokenKey = getJwtTokenKey(authentication.getName());
        if (jwtToken.getClaimsJws().getBody().getExpiration() == null) {
            redisTemplate.opsForValue().set(jwtTokenKey, jwtToken);
        } else {
            long timeout = jwtToken.getClaimsJws().getBody().getExpiration().getTime() - System.currentTimeMillis();
            redisTemplate.opsForValue().set(jwtTokenKey, jwtToken, timeout, TimeUnit.MILLISECONDS);
        }
//        redisTemplate.executePipelined((RedisCallback<Void>) connection -> {
//            return null;
//        });
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return false;
    }

    private String getSecurityContextKey(String username) {
        // {redisNamespace}:{SecurityContextKey}:{username}
        return String.format("%s:%s:%s", redisNamespace, SecurityContextKey, username);
    }

    private String getJwtTokenKey(String username) {
        // {redisNamespace}:{JwtTokenKey}:{username}:{tokenId}
        long tokenId = SnowFlake.SNOW_FLAKE.nextId();
        return String.format("%s:%s:%s:%s", redisNamespace, JwtTokenKey, username, tokenId);
    }
}
