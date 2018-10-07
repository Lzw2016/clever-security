package org.clever.security.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.model.response.AjaxMessage;
import org.clever.common.server.controller.BaseController;
import org.clever.security.service.ManageByQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-02 20:50 <br/>
 */
@Api(description = "管理页面查询")
@RestController
@RequestMapping("/api/manage")
public class ManageByQueryController extends BaseController {

    @Autowired
    private ManageByQueryService manageByQueryService;

    @ApiOperation("查询username是否存在")
    @GetMapping("/user/username/{username}/exists")
    public AjaxMessage<Boolean> existsUserByUsername(@PathVariable("username") String username) {
        return new AjaxMessage<>(manageByQueryService.existsUserByUsername(username), "查询成功");
    }

    @ApiOperation("查询telephone是否存在")
    @GetMapping("/user/telephone/{telephone}/exists")
    public AjaxMessage<Boolean> existsUserByTelephone(@PathVariable("telephone") String telephone) {
        return new AjaxMessage<>(manageByQueryService.existsUserByTelephone(telephone), "查询成功");
    }

    @ApiOperation("查询email是否存在")
    @GetMapping("/user/email/{email}/exists")
    public AjaxMessage<Boolean> existsUserByEmail(@PathVariable("email") String email) {
        return new AjaxMessage<>(manageByQueryService.existsUserByEmail(email), "查询成功");
    }

    @ApiOperation("查询所有系统名称")
    @GetMapping("/sys_name")
    public List<String> allSysName() {
        return manageByQueryService.allSysName();
    }
}
