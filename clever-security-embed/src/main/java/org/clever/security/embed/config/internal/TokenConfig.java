package org.clever.security.embed.config.internal;

import lombok.Data;

import java.time.Duration;

/**
 * JWT Token配置
 * 作者： lzw<br/>
 * 创建时间：2019-04-25 19:04 <br/>
 */
@Data
public class TokenConfig {
    /**
     * Token Redis前缀
     */
    private String redisNamespace = "jwt";
    /**
     * Token签名密钥
     */
    private String secretKey = "clever-security-jwt";
    /**
     * Token有效时间(默认：7天)
     */
    private Duration tokenValidity = Duration.ofDays(7);
    /**
     * 登录时启用“记住我”时,Token有效时间(默认：15天)
     */
    private Duration tokenValidityForRememberMe = Duration.ofDays(15);
    /**
     * 刷新令牌有效时间
     */
    private Duration refreshTokenValidity = Duration.ofDays(30);
    /**
     * 设置密钥过期时间(格式 HH:mm:ss)
     */
    private String hoursInDay = "03:45:00";
    /**
     * iss（签发者）
     */
    private String issuer = "clever-security-jwt";
    /**
     * aud（接收方）
     */
    private String audience = "clever-*";
    /**
     * 使用Cookie传输JWT-Token(false表示使用Http Header传输JWT-Token)
     */
    private boolean useCookie = true;
    /**
     * JWT-Token名称(Cookie或Header中的key)
     */
    private String jwtTokenName = "Authorization";
}
