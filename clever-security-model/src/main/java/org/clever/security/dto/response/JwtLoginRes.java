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

    public JwtLoginRes(Boolean success, String message, String token) {
        super(success, message);
        this.token = token;
    }

    public JwtLoginRes(Boolean success, String message, UserRes user, String token) {
        super(success, message, user);
        this.token = token;
    }
}
