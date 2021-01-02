package org.clever.security.controller;

import org.clever.security.client.AuthSupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.GetAllApiPermissionRes;
import org.clever.security.dto.response.GetApiPermissionRes;
import org.clever.security.dto.response.RegisterApiPermissionRes;
import org.clever.security.model.SecurityContext;
import org.clever.security.service.AuthSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/18 22:17 <br/>
 */
@RestController
@RequestMapping("/security/api")
public class AuthSupportController implements AuthSupportClient {
    @Autowired
    private AuthSupportService authSupportService;

    /**
     * 获取API权限信息
     */
    @GetMapping("/api_permission")
    @Override
    public GetApiPermissionRes getApiPermission(@Validated GetApiPermissionReq req) {
        return authSupportService.getApiPermission(req);
    }

    /**
     * 缓存SecurityConfig对象
     */
    @GetMapping("/cache_security_context")
    @Override
    public void cacheContext(@Validated CacheContextReq req) {
        authSupportService.cacheContext(req);
    }

    /**
     * 加载SecurityConfig对象
     */
    @GetMapping("/load_security_context")
    @Override
    public SecurityContext loadContext(@Validated LoadContextReq req) {
        return authSupportService.loadContext(req);
    }

    /**
     * 获取系统所有的API权限
     */
    @GetMapping("/all_api_permission")
    @Override
    public GetAllApiPermissionRes getAllApiPermission(@Validated GetAllApiPermissionReq req) {
        return null;
    }

    /**
     * 注册系统API权限
     */
    @PostMapping("/register_api_permission")
    @Override
    public RegisterApiPermissionRes registerApiPermission(@Validated @RequestBody RegisterApiPermissionReq req) {
        return authSupportService.registerApiPermission(req);
    }
}
