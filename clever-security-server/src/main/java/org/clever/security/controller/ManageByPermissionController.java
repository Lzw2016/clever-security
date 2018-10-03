package org.clever.security.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.server.controller.BaseController;
import org.clever.security.dto.request.RolePermissionQueryReq;
import org.clever.security.entity.model.WebPermissionModel;
import org.clever.security.service.ManageByPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public IPage<WebPermissionModel> findByPage(RolePermissionQueryReq queryReq) {
        return manageByPermissionService.findByPage(queryReq);

    }
}
