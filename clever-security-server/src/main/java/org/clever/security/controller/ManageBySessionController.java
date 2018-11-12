package org.clever.security.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation("重新加载用户Session权限")
    @GetMapping("/reload_session_security_context/{username}")
    public Map<String, SecurityContext> reloadSessionSecurityContext(@PathVariable("username") String username) {
        return sessionService.reloadSessionSecurityContext(username);
    }
}
