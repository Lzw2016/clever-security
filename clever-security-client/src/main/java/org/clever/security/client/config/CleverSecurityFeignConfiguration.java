package org.clever.security.client.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-05-15 17:58 <br/>
 */
@Slf4j
public class CleverSecurityFeignConfiguration implements RequestInterceptor {

    /**
     * 服务访问Token
     */
    private final CleverSecurityAccessTokenConfig accessTokenConfig;

    public CleverSecurityFeignConfiguration(CleverSecurityAccessTokenConfig accessTokenConfig) {
        this.accessTokenConfig = accessTokenConfig;
    }

    @Override
    public void apply(RequestTemplate template) {
        if (accessTokenConfig.getAccessTokenHeads() != null && accessTokenConfig.getAccessTokenHeads().size() > 0) {
            // log.debug("[访问clever-security-server的AccessToken请求头] --> {}", accessTokenConfig.getAccessTokenHeads());
            for (Map.Entry<String, String> entry : accessTokenConfig.getAccessTokenHeads().entrySet()) {
                template.header(entry.getKey(), entry.getValue());
            }
        }
    }
}
