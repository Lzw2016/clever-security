package org.clever.security.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.RoleQueryReq;
import org.clever.security.dto.response.admin.RoleQueryRes;
import org.clever.security.service.admin.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/17 12:58 <br/>
 */
@RestController
@RequestMapping("/security/admin/api")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/role/page_query")
    public IPage<RoleQueryRes> pageQuery(RoleQueryReq req) {
        return roleService.pageQuery(req);
    }
}
