package org.clever.security.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.server.controller.BaseController;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.dto.request.RememberMeTokenAddReq;
import org.clever.security.dto.request.RememberMeTokenUpdateReq;
import org.clever.security.entity.RememberMeToken;
import org.clever.security.service.RememberMeTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-24 16:36 <br/>
 */
@Api(description = "RememberMeToken")
@RestController
@RequestMapping("/api")
public class RememberMeTokenController extends BaseController {

    @Autowired
    private RememberMeTokenService rememberMeTokenService;

    @ApiOperation("新增RememberMeToken")
    @PostMapping("/remember_me_token")
    public RememberMeToken addRememberMeToken(@RequestBody @Validated RememberMeTokenAddReq req) {
        RememberMeToken rememberMeToken = BeanMapper.mapper(req, RememberMeToken.class);
        return rememberMeTokenService.addRememberMeToken(rememberMeToken);
    }

    @ApiOperation("读取RememberMeToken")
    @GetMapping("/remember_me_token/series")
    public RememberMeToken getRememberMeToken(@RequestParam("series") String series) {
        return rememberMeTokenService.getRememberMeToken(series);
    }

    @ApiOperation("删除RememberMeToken")
    @DeleteMapping("/remember_me_token/{username}")
    public Map<String, Object> delRememberMeToken(@PathVariable("username") String username) {
        Map<String, Object> map = new HashMap<>();
        Integer delCount = rememberMeTokenService.delRememberMeToken(username);
        map.put("delCount", delCount);
        return map;
    }

    @ApiOperation("修改RememberMeToken")
    @PutMapping("/remember_me_token/series")
    public RememberMeToken updateRememberMeToken(@RequestParam("series") String series, @RequestBody @Validated RememberMeTokenUpdateReq req) {
        return rememberMeTokenService.updateRememberMeToken(series, req.getToken(), req.getLastUsed());
    }
}
