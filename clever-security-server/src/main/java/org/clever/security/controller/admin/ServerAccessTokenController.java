package org.clever.security.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.ServerAccessTokenAddReq;
import org.clever.security.dto.request.admin.ServerAccessTokenQueryReq;
import org.clever.security.dto.request.admin.ServerAccessTokenUpdateReq;
import org.clever.security.dto.response.admin.ServerAccessTokenQueryRes;
import org.clever.security.entity.ServerAccessToken;
import org.clever.security.service.admin.ServerAccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/17 11:36 <br/>
 */
@RestController
@RequestMapping("/security/admin/api")
public class ServerAccessTokenController {
    @Autowired
    private ServerAccessTokenService serverAccessTokenService;

    @GetMapping("/server_access_token/page_query")
    public IPage<ServerAccessTokenQueryRes> pageQuery(ServerAccessTokenQueryReq req) {
        return serverAccessTokenService.pageQuery(req);
    }

    @PostMapping("/server_access_token/add")
    public ServerAccessToken addServerAccessToken(@RequestBody @Validated ServerAccessTokenAddReq req) {
        return serverAccessTokenService.addServerAccessToken(req);
    }

    @PutMapping("/server_access_token/update")
    public ServerAccessToken updateServerAccessToken(@RequestBody @Validated ServerAccessTokenUpdateReq req) {
        return serverAccessTokenService.updateServerAccessToken(req);
    }

    @DeleteMapping("/server_access_token/del")
    public ServerAccessToken delServerAccessToken(@RequestParam("id") Long id) {
        return serverAccessTokenService.delServerAccessToken(id);
    }
}
