package org.clever.security.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.MenuPermissionAddReq;
import org.clever.security.dto.request.admin.MenuPermissionQueryReq;
import org.clever.security.dto.request.admin.MenuPermissionUpdateReq;
import org.clever.security.dto.response.admin.MenuPermissionQueryRes;
import org.clever.security.entity.Domain;
import org.clever.security.entity.MenuPermission;
import org.clever.security.service.admin.MenuPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/17 12:58 <br/>
 */
@RestController
@RequestMapping("/security/admin/api")
public class MenuPermissionController {
    @Autowired
    private MenuPermissionService menuPermissionService;

    @GetMapping("/menu_permission/page_query")
    public IPage<MenuPermissionQueryRes> pageQuery(MenuPermissionQueryReq req) {
        return menuPermissionService.pageQuery(req);
    }

    @PostMapping("/ui_permission/add")
    public MenuPermission addUiPermission(@RequestBody @Validated MenuPermissionAddReq req) {
        return menuPermissionService.addUiPermission(req);
    }

    @PutMapping("/ui_permission/update")
    public MenuPermission updateUiPermission(@RequestBody @Validated MenuPermissionUpdateReq req) {
        return menuPermissionService.updateUiPermission(req);
    }

    @DeleteMapping("/ui_permission/del")
    public MenuPermission delUiPermission(@RequestParam("domainId") Long domainId, @RequestParam("id") Long id) {
        return menuPermissionService.delUiPermission(domainId, id);
    }
}
