package org.clever.security.jwt.repository;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.jwt.model.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-18 17:27 <br/>
 */
@Component
@Slf4j
public class JwtRedisSecurityContextRepository implements SecurityContextRepository {
    private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    @Autowired
    private RedisJwtRepository redisJwtRepository;

    /**
     * 从Redis读取Jwt Token
     */
    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        JwtToken JwtToken;
        try {
            JwtToken = redisJwtRepository.getJwtToken(request);
            log.info("### JwtToken 验证成功");
        } catch (Throwable e) {
            log.warn("### JwtToken 验证失败");
            return SecurityContextHolder.createEmptyContext();
        }
        // 读取 context
        SecurityContext securityContext;
        try {
            securityContext = redisJwtRepository.getSecurityContext(JwtToken);
            log.info("### 读取SecurityContext成功");
        } catch (Throwable e) {
            log.warn("### 读取SecurityContext失败");
            return SecurityContextHolder.createEmptyContext();
        }
        return securityContext;
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
        // 验证当前请求 JWT Token
        if (redisJwtRepository.validationToken(request)) {
            return;
        }
        // 保存 context
        redisJwtRepository.saveSecurityContext(context);
        log.info("### 已保存SecurityContext");
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        // 验证 JWT Token
        return redisJwtRepository.validationToken(request);
    }
}
