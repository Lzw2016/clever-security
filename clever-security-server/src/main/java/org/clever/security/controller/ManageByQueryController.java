package org.clever.security.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.model.response.AjaxMessage;
import org.clever.common.server.controller.BaseController;
import org.clever.security.dto.request.RememberMeTokenQueryReq;
import org.clever.security.dto.request.UserLoginLogQueryReq;
import org.clever.security.entity.model.UserLoginLogModel;
import org.clever.security.entity.model.UserRememberMeToken;
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

    @ApiOperation("查询所有系统名称")// TODO 修改
    @GetMapping("/sys_name")
    public List<String> allSysName() {
        return manageByQueryService.allSysName();
    }

    @ApiOperation("查询用户绑定的系统")
    @GetMapping("/sys_name/{username}")
    public List<String> findSysNameByUser(@PathVariable("username") String username) {
        return manageByQueryService.findSysNameByUser(username);
    }

    @ApiOperation("查询所有角色名称")
    @GetMapping("/role_name")
    public List<String> allRoleName() {
        return manageByQueryService.allRoleName();
    }

    @ApiOperation("查询用户拥有的角色")
    @GetMapping("/role_name/{username}")
    public List<String> findRoleNameByUser(@PathVariable("username") String username) {
        return manageByQueryService.findRoleNameByUser(username);
    }

    @ApiOperation("查询角色拥有的权限字符串")
    @GetMapping("/permission_str/{roleName}")
    public List<String> findPermissionStrByRole(@PathVariable("roleName") String roleName) {
        return manageByQueryService.findPermissionStrByRole(roleName);
    }

    @ApiOperation("分页查询“记住我”功能的Token")
    @GetMapping("/remember_me_token")
    public List<UserRememberMeToken> findRememberMeToken(RememberMeTokenQueryReq req) {
        return manageByQueryService.findRememberMeToken(req);
    }

    @ApiOperation("分页查询用户登录日志")
    @GetMapping("/user_login_log")
    public List<UserLoginLogModel> findUserLoginLog(UserLoginLogQueryReq req) {
        return manageByQueryService.findUserLoginLog(req);
    }
}
