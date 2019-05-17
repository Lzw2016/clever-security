package org.clever.security.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.spring.SpringContextHolder;

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
    private CleverSecurityAccessTokenConfig accessTokenConfig;

    @Override
    public void apply(RequestTemplate template) {
        if (accessTokenConfig == null) {
            accessTokenConfig = SpringContextHolder.getBean(CleverSecurityAccessTokenConfig.class);
            // log.debug("读取访问clever-security-server服务API的授权Token请求头: {}", accessTokenConfig.getAccessTokenHeads());
        }
        if (accessTokenConfig.getAccessTokenHeads() != null && accessTokenConfig.getAccessTokenHeads().size() > 0) {
            // log.debug("[访问clever-security-server的AccessToken请求头] --> {}", accessTokenConfig.getAccessTokenHeads());
            for (Map.Entry<String, String> entry : accessTokenConfig.getAccessTokenHeads().entrySet()) {
                template.header(entry.getKey(), entry.getValue());
            }
        }
    }
}
