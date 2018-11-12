package org.clever.security.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.security.dto.response.ForcedOfflineRes;
import org.clever.security.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-12 10:21 <br/>
 */
@Api(description = "Session管理")
@RestController
@RequestMapping("/api/manage")
public class ManageBySessionController {

    @Autowired
    private SessionService sessionService;

    @ApiOperation("重新加载用户SessionSecurityContext")
    @GetMapping("/reload_session_security_context/{username}")
    public Map<String, SecurityContext> reloadSessionSecurityContext(@PathVariable("username") String username) {
        return sessionService.reloadSessionSecurityContext(username);
    }

    @ApiOperation("读取用户SessionSecurityContext")
    @GetMapping("/session_security_context/{sysName}/{username}")
    public Map<String, SecurityContext> getSessionSecurityContext(@PathVariable("sysName") String sysName, @PathVariable("username") String username) {
        return sessionService.getSessionSecurityContext(sysName, username);
    }

    @ApiOperation("读取用户SessionSecurityContext")
    @GetMapping("/session_security_context/{sessionId}")
    public SecurityContext getSessionSecurityContext(@PathVariable("sessionId") String sessionId) {
        return sessionService.getSessionSecurityContext(sessionId);
    }

    @ApiOperation("踢出用户(强制下线)")
    @GetMapping("/forced_offline/{sysName}/{username}")
    public ForcedOfflineRes forcedOffline(@PathVariable("sysName") String sysName, @PathVariable("username") String username) {
        int count = sessionService.forcedOffline(sysName, username);
        ForcedOfflineRes forcedOfflineRes = new ForcedOfflineRes();
        forcedOfflineRes.setCount(count);
        forcedOfflineRes.setSysName(sysName);
        forcedOfflineRes.setUsername(username);
        return forcedOfflineRes;
    }
}
