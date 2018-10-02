package org.clever.security.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.server.controller.BaseController;
import org.clever.security.dto.request.RoleQueryPageReq;
import org.clever.security.entity.Role;
import org.clever.security.service.ManageByRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-02 20:47 <br/>
 */
@Api(description = "角色管理")
@RestController
@RequestMapping("/api/manage")
public class ManageByRoleController extends BaseController {

    @Autowired
    private ManageByRoleService manageByRoleService;

    @ApiOperation("查询权限列表")
    @GetMapping("/role")
    public IPage<Role> findByPage(RoleQueryPageReq roleQueryPageReq) {
        return manageByRoleService.findByPage(roleQueryPageReq);
    }
}

