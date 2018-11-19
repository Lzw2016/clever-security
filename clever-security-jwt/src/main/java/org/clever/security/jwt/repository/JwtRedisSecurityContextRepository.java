package org.clever.security.jwt.repository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
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
import java.util.Objects;
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
    private static final String JwtTokenHeaderKey = "Authorization";

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
        // 验证 JWT Token
        String token = request.getHeader(JwtTokenHeaderKey);
        if (StringUtils.isBlank(token)) {
            // token 为空
            return SecurityContextHolder.createEmptyContext();
        }
        Jws<Claims> claimsJws = jwtTokenService.getClaimsJws(token);
        if (claimsJws == null) {
            // token 不正确
            return SecurityContextHolder.createEmptyContext();
        }
        String jwtTokenKey = getJwtTokenKey(claimsJws.getBody());
        Object object = redisTemplate.opsForValue().get(jwtTokenKey);
        if (!(object instanceof JwtToken)) {
            // token 类型不同
            return SecurityContextHolder.createEmptyContext();
        }
        JwtToken jwtToken = (JwtToken) object;
        if (!Objects.equals(token, jwtToken.getToken())) {
            // token 不相同
            return SecurityContextHolder.createEmptyContext();
        }
        log.info("### JWTToken 验证成功");
        // 读取 context
        String securityContextKey = getSecurityContextKey(claimsJws.getBody().getSubject());
        object = redisTemplate.opsForValue().get(securityContextKey);
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
        // 判断当前请求 JWT Token
        String token = request.getHeader(JwtTokenHeaderKey);
        if (StringUtils.isNotBlank(token)) {
            try {
                Jws<Claims> claimsJws = jwtTokenService.getClaimsJws(token);
                if (claimsJws != null) {
                    return;
                }
            } catch (Throwable ignored) {
            }
        }
        // 保存 context
        String securityContextKey = getSecurityContextKey(authentication.getName());
        redisTemplate.opsForValue().set(securityContextKey, context);
        // 保存 JWT Token
        JwtToken jwtToken = jwtTokenService.createToken(authentication, false);
        String jwtTokenKey = getJwtTokenKey(jwtToken.getClaims());
        if (jwtToken.getClaims().getExpiration() == null) {
            redisTemplate.opsForValue().set(jwtTokenKey, jwtToken);
        } else {
            long timeout = jwtToken.getClaims().getExpiration().getTime() - System.currentTimeMillis();
            redisTemplate.opsForValue().set(jwtTokenKey, jwtToken, timeout, TimeUnit.MILLISECONDS);
        }
        log.info("### 已保存SecurityContext 和 JWTToken");
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return false;
    }

    private String getSecurityContextKey(String username) {
        // {redisNamespace}:{SecurityContextKey}:{username}
        return String.format("%s:%s:%s", redisNamespace, SecurityContextKey, username);
    }

    private String getJwtTokenKey(Claims claims) {
        // {redisNamespace}:{JwtTokenKey}:{username}:{JWT ID}
        return String.format("%s:%s:%s:%s", redisNamespace, JwtTokenKey, claims.getSubject(), claims.getId());
    }
}
