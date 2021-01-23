package org.clever.security.client;

import org.clever.security.Constant;
import org.clever.security.client.config.CleverSecurityFeignConfiguration;
import org.clever.security.entity.ServerAccessToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/23 17:20 <br/>
 */
@FeignClient(
        contextId = "org.clever.security.client.ServerAccessSupportClient",
        name = Constant.ServerName,
        path = "/security/api",
        configuration = CleverSecurityFeignConfiguration.class
)
public interface ServerAccessSupportClient {
    /**
     * 获取所有有效的ServerAccessToken
     */
    @GetMapping("/server_access_token/all_effective")
    List<ServerAccessToken> findAllEffectiveServerAccessToken(@RequestParam("domainId") Long domainId);
}
