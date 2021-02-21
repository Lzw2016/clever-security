package org.clever.security.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.UiPermissionQueryReq;
import org.clever.security.dto.response.admin.UiPermissionQueryRes;
import org.clever.security.service.admin.UiPermissionService;
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
public class UiPermissionController {
    @Autowired
    private UiPermissionService uiPermissionService;

    @GetMapping("/ui_permission/page_query")
    public IPage<UiPermissionQueryRes> pageQuery(UiPermissionQueryReq req) {
        return uiPermissionService.pageQuery(req);
    }

}
