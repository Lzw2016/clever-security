package org.clever.security.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.clever.security.dto.request.RefreshTokenReq;
import org.clever.security.dto.response.JwtLoginRes;
import org.clever.security.dto.response.UserRes;
import org.clever.security.repository.RedisJwtRepository;
import org.clever.security.token.JwtAccessToken;
import org.clever.security.token.JwtRefreshToken;
import org.clever.security.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-12 17:03 <br/>
 */
@Api("当前登录用户信息")
@RestController
@Slf4j
public class LoginUserController {

    @Autowired
    private RedisJwtRepository redisJwtRepository;

    @ApiOperation("获取当前登录用户信息")
    @GetMapping("/login/user_info.json")
    public UserRes currentLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return AuthenticationUtils.getUserRes(authentication);
    }

    @ApiOperation("使用刷新令牌延长登录JwtToken过期时间")
    @PutMapping("/login/refresh_token.json")
    public JwtLoginRes refreshToken(@RequestBody @Validated RefreshTokenReq refreshTokenReq) {
        JwtRefreshToken jwtRefreshToken = redisJwtRepository.getRefreshToken(refreshTokenReq.getRefreshToken());
        SecurityContext securityContext = redisJwtRepository.getSecurityContext(jwtRefreshToken.getUsername());
        // 生成新的 JwtAccessToken 和 JwtRefreshToken
        JwtAccessToken newJwtAccessToken = redisJwtRepository.saveJwtToken(AuthenticationUtils.getUserLoginToken(securityContext.getAuthentication()));
        // 删除之前的令牌
        JwtAccessToken oldJwtAccessToken = null;
        try {
            oldJwtAccessToken = redisJwtRepository.getJwtToken(jwtRefreshToken.getUsername(), jwtRefreshToken.getTokenId());
        } catch (Throwable ignored) {
        }
        if (oldJwtAccessToken != null) {
            redisJwtRepository.deleteJwtToken(oldJwtAccessToken);
        }
        return new JwtLoginRes(
                true,
                "刷新登录令牌成功",
                AuthenticationUtils.getUserRes(securityContext.getAuthentication()),
                newJwtAccessToken.getToken(),
                newJwtAccessToken.getRefreshToken()
        );
    }
}
