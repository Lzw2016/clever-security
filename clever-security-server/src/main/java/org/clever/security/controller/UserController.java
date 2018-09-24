package org.clever.security.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.server.controller.BaseController;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.dto.request.UserAddReq;
import org.clever.security.dto.response.UserAddRes;
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
@Api(description = "用户信息")
@RestController
@RequestMapping("/api")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @ApiOperation("新增用户")
    @PostMapping("/user")
    public UserAddRes addUser(@RequestBody @Validated UserAddReq userAddReq) {
        User user = BeanMapper.mapper(userAddReq, User.class);
        user = userService.addUser(user);
        return BeanMapper.mapper(user, UserAddRes.class);
    }

    @ApiOperation("获取用户")
    @GetMapping("/user/{usernameOrTelephone}")
    public User getUser(@PathVariable("usernameOrTelephone") String usernameOrTelephone) {
        return userService.getUser(usernameOrTelephone);
    }

    @ApiOperation("获取某个用户的所有权限")
    @GetMapping("/user/{username}/permission")
    public List<Permission> findAllPermission(@PathVariable("username") String username) {
        return userService.findAllPermission(username);
    }

    // 更新用户

    // 删除用户

    // 查询单个用户-仅用户信息

    // 查询单个用户-所有信息

    // 查询用户列表
}
