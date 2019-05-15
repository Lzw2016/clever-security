package org.clever.security.client;

import org.clever.security.config.CleverSecurityFeignConfiguration;
import org.clever.security.dto.request.UserAuthenticationReq;
import org.clever.security.dto.response.UserAuthenticationRes;
import org.clever.security.entity.Permission;
import org.clever.security.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-24 20:17 <br/>
 */
@FeignClient(
        contextId = "org.clever.security.client.UserClient",
        name = "clever-security-server",
        path = "/api",
        configuration = CleverSecurityFeignConfiguration.class
)
public interface UserClient {

    /**
     * 获取用户
     */
    @GetMapping("/user/{unique}")
    User getUser(@PathVariable("unique") String unique);

    /**
     * 获取某个用户在某个系统下的所有权限
     */
    @GetMapping("/user/{username}/{sysName}/permission")
    List<Permission> findAllPermission(@PathVariable("username") String username, @PathVariable("sysName") String sysName);

    /**
     * 用户是否有权登录某个系统
     */
    @GetMapping("/user/{username}/{sysName}")
    Boolean canLogin(@PathVariable("username") String username, @PathVariable("sysName") String sysName);

    /**
     * 用户登录认证
     */
    @PostMapping("/user/authentication")
    UserAuthenticationRes authentication(@RequestBody UserAuthenticationReq req);
}
