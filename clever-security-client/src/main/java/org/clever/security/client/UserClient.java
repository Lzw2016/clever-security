package org.clever.security.client;

import org.clever.security.entity.Permission;
import org.clever.security.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-24 20:17 <br/>
 */
@FeignClient(name = "clever-security-server", path = "/api")
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
}
