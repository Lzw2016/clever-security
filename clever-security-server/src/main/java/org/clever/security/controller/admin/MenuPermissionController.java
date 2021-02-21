package org.clever.security.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.MenuPermissionQueryReq;
import org.clever.security.dto.response.admin.MenuPermissionQueryRes;
import org.clever.security.service.admin.MenuPermissionService;
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
public class MenuPermissionController {
    @Autowired
    private MenuPermissionService menuPermissionService;

    @GetMapping("/menu_permission/page_query")
    public IPage<MenuPermissionQueryRes> pageQuery(MenuPermissionQueryReq req) {
        return menuPermissionService.pageQuery(req);
    }
}
