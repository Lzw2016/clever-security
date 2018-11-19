package org.clever.security.jwt.repository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.security.jwt.model.JwtToken;
import org.clever.security.jwt.service.GenerateKeyService;
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

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-18 17:27 <br/>
 */
@Component
@Slf4j
public class JwtRedisSecurityContextRepository implements SecurityContextRepository {
    private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    @Autowired
    private JWTTokenService jwtTokenService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private GenerateKeyService generateKeyService;

    /**
     * 从Redis读取Jwt Token
     */
    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        // 验证 JWT Token
        String token = request.getHeader(GenerateKeyService.JwtTokenHeaderKey);
        if (StringUtils.isBlank(token)) {
            // token 为空
            return SecurityContextHolder.createEmptyContext();
        }
        Jws<Claims> claimsJws = jwtTokenService.getClaimsJws(token);
        if (claimsJws == null) {
            // token 不正确
            return SecurityContextHolder.createEmptyContext();
        }
        String jwtTokenKey = generateKeyService.getJwtTokenKey(claimsJws.getBody());
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
        String securityContextKey = generateKeyService.getSecurityContextKey(claimsJws.getBody().getSubject());
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
        String token = request.getHeader(GenerateKeyService.JwtTokenHeaderKey);
        if (StringUtils.isNotBlank(token)) {
            try {
                if (jwtTokenService.validationToken(token)) {
                    return;
                }
            } catch (Throwable ignored) {
            }
        }
        // 保存 context
        String securityContextKey = generateKeyService.getSecurityContextKey(authentication.getName());
        redisTemplate.opsForValue().set(securityContextKey, context);
        log.info("### 已保存SecurityContext");
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        // 验证 JWT Token
        String token = request.getHeader(GenerateKeyService.JwtTokenHeaderKey);
        if (StringUtils.isBlank(token)) {
            // token 为空
            return false;
        }
        return jwtTokenService.validationToken(token);
    }
}
