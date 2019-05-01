package org.clever.security.client;

import org.clever.security.dto.request.UserLoginLogAddReq;
import org.clever.security.dto.request.UserLoginLogUpdateReq;
import org.clever.security.entity.UserLoginLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-24 19:25 <br/>
 */
@FeignClient(contextId = "org.clever.security.client.UserLoginLogClient", name = "clever-security-server", path = "/api")
public interface UserLoginLogClient {

    /**
     * 新增登录日志
     */
    @PostMapping("/user_login_log")
    UserLoginLog addUserLoginLog(@RequestBody @Validated UserLoginLogAddReq req);

    /**
     * 根据SessionID查询用户登录日志
     */
    @GetMapping("/user_login_log/{sessionId}")
    UserLoginLog getUserLoginLog(@PathVariable("sessionId") String sessionId);

    /**
     * 更新登录日志信息
     */
    @PutMapping("/user_login_log/{sessionId}")
    UserLoginLog updateUserLoginLog(@PathVariable("sessionId") String sessionId, @RequestBody UserLoginLogUpdateReq req);
}
