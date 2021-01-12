package org.clever.security.client;

import org.clever.security.Constant;
import org.clever.security.client.config.CleverSecurityFeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/12 21:19 <br/>
 */
@FeignClient(
        contextId = "org.clever.security.client.RegisterSupportClient",
        name = Constant.ServerName,
        path = "/security/api",
        configuration = CleverSecurityFeignConfiguration.class
)
public interface PasswordRecoverySupportClient {
}
