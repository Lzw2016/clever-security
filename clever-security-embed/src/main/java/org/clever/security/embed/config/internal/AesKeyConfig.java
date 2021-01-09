package org.clever.security.embed.config.internal;

import lombok.Data;

import java.io.Serializable;

/**
 * AES对称加密key配置
 * 作者： lzw<br/>
 * 创建时间：2019-04-25 19:03 <br/>
 */
@Data
public class AesKeyConfig implements Serializable {
    /**
     * 启用密码加密传输
     */
    private boolean enable = true;
    /**
     * 密码AES加密 key(Hex编码) -- 请求数据，与前端一致
     */
    private String reqPasswordAesKey = "b9049bb512f6b776835b9bf9e6c44c45";
    /**
     * 密码AES加密 iv(Hex编码) -- 请求数据，与前端一致
     */
    private String reqPasswordAesIv = "47e2cf4f8ee69fd9d7f3cad475682df8";
}
