package org.clever.security.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.security.config.SecurityConfig;
import org.clever.security.config.model.ServerApiAccessToken;
import org.clever.security.token.JwtAccessToken;
import org.clever.security.token.ServerApiAccessContextToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
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
    private SecurityConfig securityConfig;
    @Autowired
    private RedisJwtRepository redisJwtRepository;

    /**
     * 从Redis读取Jwt Token
     */
    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        if (userServerApiAccessToken()) {
            ServerApiAccessToken serverApiAccessToken = securityConfig.getServerApiAccessToken();
            if (validationServerApiAccessToken(request)) {
                ServerApiAccessContextToken serverApiAccessContextToken = new ServerApiAccessContextToken(serverApiAccessToken.getTokenName(), serverApiAccessToken.getTokenValue());
                log.info("[ServerApiAccessContextToken]创建成功 -> {}", serverApiAccessContextToken);
                return new SecurityContextImpl(serverApiAccessContextToken);
            }
        }
        JwtAccessToken jwtAccessToken;
        try {
            jwtAccessToken = redisJwtRepository.getJwtToken(request);
            log.info("### JwtToken 验证成功");
        } catch (Throwable e) {
            if (e instanceof BusinessException) {
                log.warn("### JwtToken 验证失败: {}", e.getMessage());
            } else {
                log.warn("### JwtToken 验证失败", e);
            }
            return SecurityContextHolder.createEmptyContext();
        }
        // 读取 context
        SecurityContext securityContext;
        try {
            securityContext = redisJwtRepository.getSecurityContext(jwtAccessToken);
            log.info("### 读取SecurityContext成功");
        } catch (Throwable e) {
            throw new InternalAuthenticationServiceException("读取SecurityContext失败", e);
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
        if (authentication instanceof ServerApiAccessContextToken) {
            log.info("[ServerApiAccessContextToken]不需要持久化保存 -> {}", authentication);
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
        if (userServerApiAccessToken()) {
            if (validationServerApiAccessToken(request)) {
                return true;
            }
        }
        // 验证 JWT Token
        return redisJwtRepository.validationToken(request);
    }

    /**
     * 验证请求使用的 ServerApiAccessToken
     */
    private boolean validationServerApiAccessToken(HttpServletRequest request) {
        ServerApiAccessToken serverApiAccessToken = securityConfig.getServerApiAccessToken();
        String token = request.getHeader(serverApiAccessToken.getTokenName());
        boolean result = Objects.equals(token, serverApiAccessToken.getTokenValue());
        if (!result) {
            log.warn("[ServerApiAccessContextToken]ServerApiAccessToken验证失败 -> {}", token);
        }
        return result;
    }

    /**
     * 是否使用了 ServerApiAccessToken
     */
    private boolean userServerApiAccessToken() {
        return securityConfig != null
                && securityConfig.getServerApiAccessToken() != null
                && !StringUtils.isBlank(securityConfig.getServerApiAccessToken().getTokenName())
                && !StringUtils.isBlank(securityConfig.getServerApiAccessToken().getTokenValue());
    }
}
