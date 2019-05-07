package org.clever.security.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.server.controller.BaseController;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.dto.request.RoleAddReq;
import org.clever.security.dto.request.RoleQueryPageReq;
import org.clever.security.dto.request.RoleUpdateReq;
import org.clever.security.dto.response.RoleInfoRes;
import org.clever.security.entity.Role;
import org.clever.security.service.ManageByRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-02 20:47 <br/>
 */
@Api("角色管理")
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

    @ApiOperation("新增权限")
    @PostMapping("/role")
    public Role addRole(@RequestBody @Validated RoleAddReq roleAddReq) {
        Role role = BeanMapper.mapper(roleAddReq, Role.class);
        return manageByRoleService.addRole(role);
    }

    @ApiOperation("更新权限")
    @PutMapping("/role/{name}")
    public Role updateRole(@PathVariable("name") String name, @RequestBody @Validated RoleUpdateReq roleUpdateReq) {
        return manageByRoleService.updateRole(name, roleUpdateReq);
    }

    @ApiOperation("删除权限")
    @DeleteMapping("/role/{name}")
    public Role delRole(@PathVariable("name") String name) {
        return manageByRoleService.delRole(name);
    }

    @ApiOperation("获取权限信息")
    @GetMapping("/role/{name}")
    public RoleInfoRes getRoleInfo(@PathVariable("name") String name) {
        return manageByRoleService.getRoleInfo(name);
    }
}

