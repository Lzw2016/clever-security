package org.clever.security.client.config;

import lombok.Data;

import java.util.Map;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-15 14:31 <br/>
 */
//@ConfigurationProperties(prefix = Constant.ConfigPrefix)
//@Component
@Data
public class CleverSecurityAccessTokenConfig {

    /**
     * 访问 clever-security-server 服务API的授权Token请求头
     */
    private Map<String, String> accessTokenHeads;
}
