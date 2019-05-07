package org.clever.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import lombok.Data;

import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-18 22:27 <br/>
 */
@Data
public class JwtAccessToken implements Serializable {

    /**
     * JWT Token 字符串
     */
    private String token;

    /**
     * JWT 刷新 Token 字符串
     */
    private String refreshToken;

    /**
     * JWT Token Header
     */
    private JwsHeader header;

    /**
     * JWT Token Body
     */
    private Claims claims;
}
