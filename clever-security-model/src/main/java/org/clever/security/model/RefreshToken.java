package org.clever.security.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-20 9:11 <br/>
 */
@Data
public class RefreshToken implements Serializable {

    /**
     * 用户名
     */
    private String username;

    /**
     * Token ID
     */
    private String tokenId;

    public RefreshToken() {
    }

    public RefreshToken(String username, String tokenId) {
        this.username = username;
        this.tokenId = tokenId;
    }
}
