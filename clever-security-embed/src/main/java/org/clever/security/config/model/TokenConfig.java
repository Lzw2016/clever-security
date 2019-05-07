package org.clever.security.config.model;

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
     * Token记住我有效时间(默认：15天)
     */
    private Duration tokenValidityForRememberMe = Duration.ofDays(15);

    /**
     * 刷新令牌有效时间
     */
    private Duration refreshTokenValidity = Duration.ofDays(30);

    /**
     * 设置密钥过期时间 (格式 HH:mm:ss)
     */
    private String hoursInDay = "03:45:00";
}
