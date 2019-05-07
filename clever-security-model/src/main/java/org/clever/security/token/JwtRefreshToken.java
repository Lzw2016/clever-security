package org.clever.security.token;

import lombok.Data;

import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-20 9:11 <br/>
 */
@Data
public class JwtRefreshToken implements Serializable {

    /**
     * 用户名
     */
    private String username;

    /**
     * Token ID
     */
    private String tokenId;

    public JwtRefreshToken() {
    }

    public JwtRefreshToken(String username, String tokenId) {
        this.username = username;
        this.tokenId = tokenId;
    }
}
