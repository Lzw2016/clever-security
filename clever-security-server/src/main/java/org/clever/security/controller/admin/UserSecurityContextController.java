package org.clever.security.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.UserSecurityContextQueryReq;
import org.clever.security.dto.response.admin.UserSecurityContextQueryRes;
import org.clever.security.entity.UserSecurityContext;
import org.clever.security.service.admin.UserSecurityContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/18 15:26 <br/>
 */
@RestController
@RequestMapping("/security/admin/api")
public class UserSecurityContextController {
    @Autowired
    private UserSecurityContextService userSecurityContextService;

    @GetMapping("/user_security_context/page_query")
    public IPage<UserSecurityContextQueryRes> pageQuery(UserSecurityContextQueryReq req) {
        return userSecurityContextService.pageQuery(req);
    }

    @PostMapping("/user_security_context/reload")
    public UserSecurityContext reloadUserSecurityContext(@RequestParam("id") Long id) {
        return userSecurityContextService.reloadUserSecurityContext(id);
    }
}
