package org.clever.security.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.UiPermissionAddReq;
import org.clever.security.dto.request.admin.UiPermissionQueryReq;
import org.clever.security.dto.request.admin.UiPermissionUpdateReq;
import org.clever.security.dto.response.admin.MenuAndUiPermissionTreeRes;
import org.clever.security.dto.response.admin.UiPermissionQueryRes;
import org.clever.security.entity.UiPermission;
import org.clever.security.service.admin.UiPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/17 12:58 <br/>
 */
@RestController
@RequestMapping("/security/admin/api")
public class UiPermissionController {
    @Autowired
    private UiPermissionService uiPermissionService;

    @GetMapping("/ui_permission/menu_and_ui_tree")
    public List<MenuAndUiPermissionTreeRes> menuAndUiTree(@RequestParam("domainId") Long domainId) {
        return uiPermissionService.menuAndUiTree(domainId);
    }

    @GetMapping("/ui_permission/find_ui_by_menu")
    public List<UiPermissionQueryRes> findUiByMenu(@RequestParam("menuId") Long menuId) {
        return uiPermissionService.findUiByMenu(menuId);
    }

    @PostMapping("/ui_permission/add")
    public UiPermission addUiPermission(@RequestBody @Validated UiPermissionAddReq req) {
        return uiPermissionService.addUiPermission(req);
    }

    @PutMapping("/ui_permission/update")
    public UiPermission updateUiPermission(@RequestBody @Validated UiPermissionUpdateReq req) {
        return uiPermissionService.updateUiPermission(req);
    }

    @DeleteMapping("/ui_permission/del")
    public UiPermission delUiPermission(@RequestParam("id") Long id) {
        return uiPermissionService.delUiPermission(id);
    }

    @GetMapping("/ui_permission/page_query")
    public IPage<UiPermissionQueryRes> pageQuery(UiPermissionQueryReq req) {
        return uiPermissionService.pageQuery(req);
    }
}
