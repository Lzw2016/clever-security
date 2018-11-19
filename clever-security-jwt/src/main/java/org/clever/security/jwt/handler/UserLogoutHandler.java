package org.clever.security.jwt.handler;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.security.jwt.service.GenerateKeyService;
import org.clever.security.jwt.service.JWTTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
@Component
@Slf4j
public class UserLogoutHandler implements LogoutHandler {

    @Autowired
    private JWTTokenService jwtTokenService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private GenerateKeyService generateKeyService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // UserLoginToken userLoginToken = AuthenticationUtils.getUserLoginToken(authentication);
        String token = request.getHeader(GenerateKeyService.JwtTokenHeaderKey);
        if (StringUtils.isNotBlank(token)) {
            Jws<Claims> claimsJws = jwtTokenService.getClaimsJws(token);
            String jwtTokenKey = generateKeyService.getJwtTokenKey(claimsJws.getBody());
            Boolean flag = redisTemplate.hasKey(jwtTokenKey);
            if (flag != null && flag) {
                flag = redisTemplate.delete(jwtTokenKey);
                if (flag == null || !flag) {
                    throw new BusinessException("登出失败");
                } else {
                    log.info("### 删除 JWT Token成功");
                }
            }
            Set<String> ketSet = redisTemplate.keys(generateKeyService.getJwtTokenPatternKey(claimsJws.getBody().getSubject()));
            String securityContextKey = generateKeyService.getSecurityContextKey(claimsJws.getBody().getSubject());
            flag = redisTemplate.hasKey(securityContextKey);
            if (ketSet != null && ketSet.size() <= 0 && flag != null && flag) {
                flag = redisTemplate.delete(securityContextKey);
                if (flag != null && flag) {
                    log.info("### 删除 SecurityContext 成功");
                }
            }
        }
    }
}
