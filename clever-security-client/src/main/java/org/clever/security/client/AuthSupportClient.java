package org.clever.security.client;

import org.clever.security.Constant;
import org.clever.security.client.config.CleverSecurityFeignConfiguration;
import org.clever.security.dto.request.GetApiPermissionReq;
import org.clever.security.dto.response.GetApiPermissionRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/14 19:12 <br/>
 */
@FeignClient(
        contextId = "org.clever.security.client.AuthSupportClient",
        name = Constant.ServerName,
        path = "/security/api",
        configuration = CleverSecurityFeignConfiguration.class
)
public interface AuthSupportClient {
    /**
     * 获取API权限信息
     */
    @GetMapping("/api_permission")
    GetApiPermissionRes getApiPermission(@Validated GetApiPermissionReq req);
}
