package org.clever.security.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.security.LoginModel;
import org.clever.security.config.SecurityConfig;
import org.clever.security.config.model.ServerApiAccessToken;
import org.clever.security.token.ServerApiAccessContextToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-05-16 14:28 <br/>
 */
@Component
@Slf4j
public class SecurityContextRepositoryProxy implements SecurityContextRepository {

    private SecurityConfig securityConfig;
    @Autowired(required = false)
    private JwtRedisSecurityContextRepository jwtRedisSecurityContextRepository;
    @Autowired(required = false)
    private HttpSessionSecurityContextRepository httpSessionSecurityContextRepository;

    public SecurityContextRepositoryProxy(@Autowired SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
        if (LoginModel.session.equals(securityConfig.getLoginModel())) {
            httpSessionSecurityContextRepository = new HttpSessionSecurityContextRepository();
        }
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        if (userServerApiAccessToken()) {
            ServerApiAccessToken serverApiAccessToken = securityConfig.getServerApiAccessToken();
            if (validationServerApiAccessToken(requestResponseHolder.getRequest())) {
                ServerApiAccessContextToken serverApiAccessContextToken = new ServerApiAccessContextToken(serverApiAccessToken.getTokenName(), serverApiAccessToken.getTokenValue());
                // log.info("[ServerApiAccessContextToken]创建成功 -> {}", serverApiAccessContextToken);
                return new SecurityContextImpl(serverApiAccessContextToken);
            }
        }
        return getSecurityContextRepository().loadContext(requestResponseHolder);
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        if (context.getAuthentication() instanceof ServerApiAccessContextToken) {
            // log.info("[ServerApiAccessContextToken]不需要持久化保存 -> {}", context.getAuthentication());
            return;
        }
        getSecurityContextRepository().saveContext(context, request, response);
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        if (userServerApiAccessToken()) {
            if (validationServerApiAccessToken(request)) {
                return true;
            }
        }
        return getSecurityContextRepository().containsContext(request);
    }

    private SecurityContextRepository getSecurityContextRepository() {
        if (LoginModel.jwt.equals(securityConfig.getLoginModel())) {
            return jwtRedisSecurityContextRepository;
        } else if (LoginModel.session.equals(securityConfig.getLoginModel())) {
            return httpSessionSecurityContextRepository;
        }
        throw new InternalAuthenticationServiceException("未知的登入模式");
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
