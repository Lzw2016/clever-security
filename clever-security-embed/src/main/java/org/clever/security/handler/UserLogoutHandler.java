package org.clever.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.Constant;
import org.clever.security.LoginModel;
import org.clever.security.config.SecurityConfig;
import org.clever.security.repository.RedisJwtRepository;
import org.clever.security.token.JwtAccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * 用户登出处理
 * 作者： lzw<br/>
 * 创建时间：2018-11-19 15:13 <br/>
 */
@ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "loginModel", havingValue = "jwt")
@Component
@Slf4j
public class UserLogoutHandler implements LogoutHandler {

    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private RedisJwtRepository redisJwtRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (!LoginModel.jwt.equals(securityConfig.getLoginModel())) {
            return;
        }
        JwtAccessToken jwtAccessToken = null;
        try {
            jwtAccessToken = redisJwtRepository.getJwtToken(request);
        } catch (Throwable ignored) {
        }
        if (jwtAccessToken != null) {
            redisJwtRepository.deleteJwtToken(jwtAccessToken);
            log.info("### 删除 JWT Token成功");
            Set<String> ketSet = redisJwtRepository.getJwtTokenPatternKey(jwtAccessToken.getClaims().getSubject());
            if (ketSet != null && ketSet.size() <= 0) {
                redisJwtRepository.deleteSecurityContext(jwtAccessToken.getClaims().getSubject());
                log.info("### 删除 SecurityContext 成功");
            }
        }
    }
}
