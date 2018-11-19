package org.clever.security.jwt.model;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import lombok.Data;

import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-18 22:27 <br/>
 */
@Data
public class JwtToken implements Serializable {

    /**
     * JWT Token 字符串
     */
    private String token;

    /**
     * JWT Token Header
     */
    private JwsHeader header;

    /**
     * JWT Token Body
     */
    private Claims claims;
}
