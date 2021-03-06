package org.clever.security.config.model;

import lombok.Data;

/**
 * AES对称加密key配置
 * 作者： lzw<br/>
 * 创建时间：2019-04-25 19:03 <br/>
 */
@Data
public class AesKeyConfig {
    /**
     * 密码AES加密 key(Hex编码) -- 请求数据，与前端一致
     */
    private String reqPasswordAesKey;

    /**
     * 密码AES加密 iv(Hex编码) -- 请求数据，与前端一致
     */
    private String reqPasswordAesIv;
}
