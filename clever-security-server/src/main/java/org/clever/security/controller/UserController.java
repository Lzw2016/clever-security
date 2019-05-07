package org.clever.security.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.server.controller.BaseController;
import org.clever.security.dto.request.UserAuthenticationReq;
import org.clever.security.dto.response.UserAuthenticationRes;
import org.clever.security.entity.Permission;
import org.clever.security.entity.User;
import org.clever.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-17 9:21 <br/>
 */
@Api("用户信息")
@RestController
@RequestMapping("/api")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @ApiOperation("获取用户(登录名、手机、邮箱)")
    @GetMapping("/user/{unique}")
    public User getUser(@PathVariable("unique") String unique) {
        return userService.getUser(unique);
    }

    @ApiOperation("获取某个用户在某个系统下的所有权限")
    @GetMapping("/user/{username}/{sysName}/permission")
    public List<Permission> findAllPermission(@PathVariable("username") String username, @PathVariable("sysName") String sysName) {
        return userService.findAllPermission(username, sysName);
    }

    @ApiOperation("用户是否有权登录某个系统")
    @GetMapping("/user/{username}/{sysName}")
    public Boolean canLogin(@PathVariable("username") String username, @PathVariable("sysName") String sysName) {
        return userService.canLogin(username, sysName);
    }

    @ApiOperation("用户登录认证")
    @PostMapping("/user/authentication")
    public UserAuthenticationRes authentication(@RequestBody @Validated UserAuthenticationReq req) {
        return userService.authenticationAndRes(req);
    }
}
