package org.clever.security.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.server.controller.BaseController;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.dto.request.UserLoginLogAddReq;
import org.clever.security.dto.request.UserLoginLogUpdateReq;
import org.clever.security.entity.UserLoginLog;
import org.clever.security.service.UserLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-24 19:21 <br/>
 */
@Api(description = "用户登录日志")
@RestController
@RequestMapping("/api")
public class UserLoginLogController extends BaseController {

    @Autowired
    private UserLoginLogService userLoginLogService;

    @ApiOperation("新增登录日志")
    @PostMapping("/user_login_log")
    public UserLoginLog addUserLoginLog(@RequestBody @Validated UserLoginLogAddReq req) {
        UserLoginLog userLoginLog = BeanMapper.mapper(req, UserLoginLog.class);
        return userLoginLogService.addUserLoginLog(userLoginLog);
    }

    @ApiOperation("根据SessionID查询用户登录日志")
    @GetMapping("/user_login_log/{sessionId}")
    public UserLoginLog getUserLoginLog(@PathVariable("sessionId") String sessionId) {
        return userLoginLogService.getUserLoginLog(sessionId);
    }

    @ApiOperation("更新登录日志信息")
    @PutMapping("/user_login_log/{sessionId}")
    public UserLoginLog updateUserLoginLog(@PathVariable("sessionId") String sessionId, @RequestBody @Validated UserLoginLogUpdateReq req) {
        UserLoginLog update = BeanMapper.mapper(req, UserLoginLog.class);
        return userLoginLogService.updateUserLoginLog(sessionId, update);
    }

}

