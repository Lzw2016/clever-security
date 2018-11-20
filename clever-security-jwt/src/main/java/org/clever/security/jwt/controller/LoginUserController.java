package org.clever.security.jwt.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.exception.BusinessException;
import org.clever.security.dto.request.RefreshTokenReq;
import org.clever.security.dto.response.JwtLoginRes;
import org.clever.security.dto.response.UserRes;
import org.clever.security.jwt.config.SecurityConfig;
import org.clever.security.jwt.model.JwtToken;
import org.clever.security.jwt.model.RefreshToken;
import org.clever.security.jwt.service.GenerateKeyService;
import org.clever.security.jwt.service.JWTTokenService;
import org.clever.security.jwt.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-12 17:03 <br/>
 */
@Api(description = "当前登录用户信息")
@RestController
@Slf4j
public class LoginUserController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private GenerateKeyService generateKeyService;
    @Autowired
    private JWTTokenService jwtTokenService;

    /**
     * 刷新令牌有效时间
     */
    private final Duration refreshTokenValidity;

    protected LoginUserController(SecurityConfig securityConfig) {
        SecurityConfig.TokenConfig tokenConfig = securityConfig.getTokenConfig();
        if (tokenConfig == null || tokenConfig.getRefreshTokenValidity() == null) {
            throw new IllegalArgumentException("未配置TokenConfig");
        }
        refreshTokenValidity = tokenConfig.getRefreshTokenValidity();
    }

    @ApiOperation("获取当前登录用户信息")
    @GetMapping("/login/user_info.json")
    public UserRes currentLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return AuthenticationUtils.getUserRes(authentication);
    }

    @ApiOperation("使用刷新令牌延长登录JwtToken过期时间")
    @PutMapping("/login/refresh_token.json")
    public JwtLoginRes refreshToken(@RequestBody @Validated RefreshTokenReq refreshTokenReq) {
        String jwtRefreshTokenKey = generateKeyService.getJwtRefreshTokenKey(refreshTokenReq.getRefreshToken());
        Boolean exists = redisTemplate.hasKey(jwtRefreshTokenKey);
        if (exists == null || !exists) {
            throw new BusinessException("刷新令牌错误");
        }
        RefreshToken refreshToken = (RefreshToken) redisTemplate.opsForValue().get(jwtRefreshTokenKey);
        // 删除原来的 JwtToken 和 RefreshToken 生成新的
        assert refreshToken != null;
        String jwtTokenKey = generateKeyService.getJwtTokenKey(refreshToken.getUsername(), refreshToken.getTokenId());
        String securityContextKey = generateKeyService.getSecurityContextKey(refreshToken.getUsername());
        SecurityContext securityContext = (SecurityContext) redisTemplate.opsForValue().get(securityContextKey);
        assert securityContext != null;
        // 登录成功保存 JWT Token
        JwtToken jwtToken = jwtTokenService.createToken(securityContext.getAuthentication(), false);
        String newJwtTokenKey = generateKeyService.getJwtTokenKey(jwtToken.getClaims());
        if (jwtToken.getClaims().getExpiration() == null) {
            redisTemplate.opsForValue().set(newJwtTokenKey, jwtToken);
        } else {
            long timeout = jwtToken.getClaims().getExpiration().getTime() - System.currentTimeMillis();
            redisTemplate.opsForValue().set(newJwtTokenKey, jwtToken, timeout, TimeUnit.MILLISECONDS);
        }
        // 保存刷新令牌 JWT Token
        String newJwtRefreshTokenKey = generateKeyService.getJwtRefreshTokenKey(jwtToken.getRefreshToken());
        RefreshToken newRefreshToken = new RefreshToken(securityContext.getAuthentication().getName(), jwtToken.getClaims().getId());
        if (refreshTokenValidity.getSeconds() <= 0) {
            redisTemplate.opsForValue().set(newJwtRefreshTokenKey, newRefreshToken);
        } else {
            redisTemplate.opsForValue().set(newJwtRefreshTokenKey, newRefreshToken, refreshTokenValidity.getSeconds(), TimeUnit.SECONDS);
        }
        // 删除之前的令牌
        redisTemplate.delete(new ArrayList<String>() {{
            add(jwtTokenKey);
            add(jwtRefreshTokenKey);
        }});
        return new JwtLoginRes(
                true,
                "刷新登录令牌成功",
                AuthenticationUtils.getUserRes(securityContext.getAuthentication()),
                jwtToken.getToken(),
                jwtToken.getRefreshToken()
        );
    }
}
