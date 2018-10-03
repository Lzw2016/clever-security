package org.clever.security.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.server.controller.BaseController;
import org.clever.security.dto.request.UserBindRoleReq;
import org.clever.security.dto.request.UserBindSysReq;
import org.clever.security.dto.response.UserBindRoleRes;
import org.clever.security.dto.response.UserBindSysRes;
import org.clever.security.service.ManageBySecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-03 21:22 <br/>
 */
@Api(description = "系统、角色、权限分配管理")
@RestController
@RequestMapping("/api/manage/security")
public class ManageBySecurityController extends BaseController {

    @Autowired
    private ManageBySecurityService manageBySecurityService;

    @ApiOperation("为用户设置登录的系统(批量)")
    @PostMapping("/user_sys")
    public List<UserBindSysRes> userBindSys(@RequestBody @Validated UserBindSysReq userBindSysReq) {
        return manageBySecurityService.userBindSys(userBindSysReq);
    }

    @ApiOperation("为用户分配角色(批量)")
    @PostMapping("/user_role")
    public List<UserBindRoleRes> userBindRole(@RequestBody @Validated UserBindRoleReq userBindSysReq) {
        return manageBySecurityService.userBindRole(userBindSysReq);
    }

    // 为角色分配权限 - (批量)

    // 为用户添加登录的系统 - 单个
    // 为用户删除登录的系统 - 单个

    // 为用户添加角色 - 单个
    // 为用户删除角色 - 单个

    // 为角色添加权限 - 单个
    // 为角色删除权限 - 单个
}
