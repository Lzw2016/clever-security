package org.clever.security.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.server.controller.BaseController;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.dto.request.UserAddReq;
import org.clever.security.dto.request.UserQueryPageReq;
import org.clever.security.dto.response.UserAddRes;
import org.clever.security.dto.response.UserInfoRes;
import org.clever.security.entity.User;
import org.clever.security.service.ManageByUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-02 20:45 <br/>
 */
@Api(description = "用户管理")
@RestController
@RequestMapping("/api/manage")
public class ManageByUserController extends BaseController {

    @Autowired
    private ManageByUserService manageByUserService;

    @ApiOperation("查询用户列表")
    @GetMapping("/user")
    public IPage<User> findByPage(UserQueryPageReq userQueryPageReq) {
        return manageByUserService.findByPage(userQueryPageReq);
    }

    @ApiOperation("获取用户详情(全部角色、权限、系统)")
    @GetMapping("/user/{username}")
    public UserInfoRes getUserInfo(@PathVariable("username") String username) {
        return manageByUserService.getUserInfo(username);
    }

    @ApiOperation("新增用户")
    @PostMapping("/user")
    public UserAddRes addUser(@RequestBody @Validated UserAddReq userAddReq) {
        User user = BeanMapper.mapper(userAddReq, User.class);
        user = manageByUserService.addUser(user);
        return BeanMapper.mapper(user, UserAddRes.class);
    }

    // 更新用户

    // 删除用户

    // 查询单个用户-仅用户信息

    // 查询单个用户-所有信息

    // 重置密码
}
