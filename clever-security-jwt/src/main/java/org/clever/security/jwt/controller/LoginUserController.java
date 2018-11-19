package org.clever.security.jwt.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.clever.security.dto.request.RefreshTokenReq;
import org.clever.security.dto.response.JwtLoginRes;
import org.clever.security.dto.response.UserRes;
import org.clever.security.jwt.service.GenerateKeyService;
import org.clever.security.jwt.service.JWTTokenService;
import org.clever.security.jwt.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation("获取当前登录用户信息")
    @GetMapping("/login/user_info.json")
    public UserRes currentLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return AuthenticationUtils.getUserRes(authentication);
    }

    @ApiOperation("使用刷新令牌延长登录JwtToken过期时间")
    @GetMapping("/login/refresh_token.json")
    public JwtLoginRes refreshToken(@RequestBody @Validated RefreshTokenReq refreshTokenReq) {
        String jwtRefreshTokenKey = generateKeyService.getJwtRefreshTokenKey(refreshTokenReq.getRefreshToken());


//        String username = jwtTokenService.getUsernameByRefreshToken(refreshTokenReq.getRefreshToken());
        // TODO 刷新令牌 UserRes
        JwtLoginRes jwtLoginRes = new JwtLoginRes(true, "刷新登录令牌成功", null, "", "");
        // 删除原来的JWT Token和刷新令牌生成新的JWT Token和刷新令牌
        return jwtLoginRes;
    }
}
