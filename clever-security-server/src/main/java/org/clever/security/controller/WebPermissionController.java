package org.clever.security.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.server.controller.BaseController;
import org.clever.security.dto.request.WebPermissionInitReq;
import org.clever.security.dto.request.WebPermissionModelGetReq;
import org.clever.security.dto.response.WebPermissionInitRes;
import org.clever.security.entity.model.WebPermissionModel;
import org.clever.security.service.WebPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-24 18:59 <br/>
 */
@Api("Web权限")
@RestController
@RequestMapping("/api")
public class WebPermissionController extends BaseController {

    @Autowired
    private WebPermissionService webPermissionService;

    @ApiOperation("根据系统和Controller信息查询Web权限")
    @GetMapping("/web_permission")
    public WebPermissionModel getWebPermissionModel(@Validated WebPermissionModelGetReq req) {
        return webPermissionService.getWebPermissionModel(req);
    }

    @ApiOperation("查询某个系统的所有Web权限")
    @GetMapping("/web_permission/{sysName}")
    public List<WebPermissionModel> findAllWebPermissionModel(@PathVariable("sysName") String sysName) {
        return webPermissionService.findAllWebPermissionModel(sysName);
    }

    @ApiOperation("初始化某个系统的所有Web权限")
    @PostMapping("/web_permission/{sysName}")
    public WebPermissionInitRes initWebPermission(@PathVariable("sysName") String sysName, @RequestBody @Validated WebPermissionInitReq req) {
        return webPermissionService.initWebPermission(sysName, req);
    }
}
