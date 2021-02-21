package org.clever.security.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.RoleAddReq;
import org.clever.security.dto.request.admin.RoleQueryReq;
import org.clever.security.dto.request.admin.RoleUpdateReq;
import org.clever.security.dto.response.admin.RoleQueryRes;
import org.clever.security.entity.Role;
import org.clever.security.service.admin.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/role/add")
    public Role addRole(@RequestBody @Validated RoleAddReq req) {
        return roleService.addRole(req);
    }

    @PutMapping("/role/update")
    public Role updateRole(@RequestBody @Validated RoleUpdateReq req) {
        return roleService.updateRole(req);
    }

    @DeleteMapping("/role/del")
    public Role delRole(@RequestParam("domainId") Long domainId, @RequestParam("id") Long id) {
        return roleService.delRole(domainId, id);
    }
}
