package org.clever.security.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.JwtTokenQueryReq;
import org.clever.security.dto.response.admin.JwtTokenQueryRes;
import org.clever.security.entity.JwtToken;
import org.clever.security.service.admin.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/17 15:31 <br/>
 */
@RestController
@RequestMapping("/security/admin/api")
public class JwtTokenController {
    @Autowired
    private JwtTokenService jwtTokenService;

    @GetMapping("/jwt_token/page_query")
    public IPage<JwtTokenQueryRes> pageQuery(JwtTokenQueryReq req) {
        return jwtTokenService.pageQuery(req);
    }

    @PostMapping("/jwt_token/disable")
    public JwtToken disableJwtToken(@RequestParam("id") Long id) {
        return jwtTokenService.disableJwtToken(id);
    }
}
