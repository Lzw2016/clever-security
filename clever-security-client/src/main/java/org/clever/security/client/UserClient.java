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
@FeignClient(name = "clever-security-server", path = "/api", url = "http://localhost:28080")
public interface UserClient {

    /**
     * 获取用户
     */
    @GetMapping("/user/{usernameOrTelephone}")
    User getUser(@PathVariable("usernameOrTelephone") String usernameOrTelephone);

    /**
     * 获取某个用户的所有权限
     */
    @GetMapping("/user/{username}/permission")
    List<Permission> findAllPermission(@PathVariable("username") String username);
}
