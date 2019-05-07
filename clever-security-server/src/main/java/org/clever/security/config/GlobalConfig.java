package org.clever.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 作者： lzw<br/>
 * 创建时间：2017-12-04 12:44 <br/>
 */
@Component
@ConfigurationProperties(prefix = "clever.config")
@Data
public class GlobalConfig {

    /**
     * 请求数据AES加密 key(Hex编码)
     */
    private String reqAesKey = "636c657665722d73656375726974792d";

    /**
     * 请求数据AES加密 iv(Hex编码)
     */
    private String reqAesIv = "f0021ea5a06d5a7bade961afe47e9ad9";
}
