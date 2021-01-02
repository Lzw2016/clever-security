package org.clever.security.client;

import org.clever.security.Constant;
import org.clever.security.client.config.CleverSecurityFeignConfiguration;
import org.clever.security.dto.request.CacheContextReq;
import org.clever.security.dto.request.GetApiPermissionReq;
import org.clever.security.dto.request.LoadContextReq;
import org.clever.security.dto.request.RegisterApiPermissionReq;
import org.clever.security.dto.response.GetApiPermissionRes;
import org.clever.security.dto.response.RegisterApiPermissionRes;
import org.clever.security.model.SecurityContext;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    GetApiPermissionRes getApiPermission(@Validated @SpringQueryMap GetApiPermissionReq req);

    /**
     * 缓存SecurityConfig对象
     */
    @GetMapping("/cache_security_context")
    void cacheContext(@Validated @SpringQueryMap CacheContextReq req);

    /**
     * 加载SecurityConfig对象
     */
    @GetMapping("/load_security_context")
    SecurityContext loadContext(@Validated @SpringQueryMap LoadContextReq req);

    /**
     * 注册系统API权限
     */
    @PostMapping("/register_api_permission")
    RegisterApiPermissionRes registerApiPermission(@Validated @RequestBody RegisterApiPermissionReq req);
}
