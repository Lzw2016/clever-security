package org.clever.security.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.server.controller.BaseController;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.dto.request.PermissionAddReq;
import org.clever.security.dto.request.PermissionQueryReq;
import org.clever.security.dto.request.PermissionUpdateReq;
import org.clever.security.entity.Permission;
import org.clever.security.entity.model.WebPermissionModel;
import org.clever.security.service.ManageByPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-02 20:48 <br/>
 */
@Api(description = "权限管理")
@RestController
@RequestMapping("/api/manage")
public class ManageByPermissionController extends BaseController {

    @Autowired
    private ManageByPermissionService manageByPermissionService;

    @ApiOperation("查询权限列表")
    @GetMapping("/permission")
    public IPage<WebPermissionModel> findByPage(PermissionQueryReq queryReq) {
        return manageByPermissionService.findByPage(queryReq);
    }

    @ApiOperation("新增权限")
    @PostMapping("/permission")
    public WebPermissionModel addPermission(@RequestBody @Validated PermissionAddReq permissionAddReq) {
        Permission permission = BeanMapper.mapper(permissionAddReq, Permission.class);
        return manageByPermissionService.addPermission(permission);
    }

    @ApiOperation("更新权限")
    @PutMapping("/permission/{permissionStr}")
    public WebPermissionModel updatePermission(
            @PathVariable("permissionStr") String permissionStr,
            @RequestBody @Validated PermissionUpdateReq permissionUpdateReq) {
        return manageByPermissionService.updatePermission(permissionStr, permissionUpdateReq);
    }

    @ApiOperation("获取权限信息")
    @GetMapping("/permission/{permissionStr}")
    public WebPermissionModel getPermissionModel(@PathVariable("permissionStr") String permissionStr) {
        return manageByPermissionService.getPermissionModel(permissionStr);
    }

    @ApiOperation("删除权限信息")
    @DeleteMapping("/permission/{permissionStr}")
    public WebPermissionModel delPermissionModel(@PathVariable("permissionStr") String permissionStr) {
        return manageByPermissionService.delPermissionModel(permissionStr);
    }

    @ApiOperation("删除权限信息(批量)")
    @DeleteMapping("/permission/batch")
    public List<WebPermissionModel> delPermissionModels(@RequestParam("permissionSet") Set<String> permissionSet) {
        if (permissionSet == null) {
            return null;
        }
        return manageByPermissionService.delPermissionModels(permissionSet);
    }
}
