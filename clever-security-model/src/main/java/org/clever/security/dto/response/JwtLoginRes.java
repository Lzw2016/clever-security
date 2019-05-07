package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-19 11:56 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JwtLoginRes extends LoginRes {

    /**
     * JWT Token 字符串
     */
    private String token;

    /**
     * JWT 刷新 Token 字符串
     */
    private String refreshToken;

    public JwtLoginRes(Boolean success, String message, String token, String refreshToken) {
        super(success, message);
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public JwtLoginRes(Boolean success, String message, UserRes user, String token, String refreshToken) {
        super(success, message, user);
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
