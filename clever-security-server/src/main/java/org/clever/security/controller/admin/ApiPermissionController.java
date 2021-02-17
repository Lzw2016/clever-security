package org.clever.security.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.ApiPermissionQueryReq;
import org.clever.security.dto.request.admin.ApiPermissionUpdateReq;
import org.clever.security.entity.ApiPermission;
import org.clever.security.service.admin.ApiPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/17 12:58 <br/>
 */
@RestController
@RequestMapping("/security/admin/api")
public class ApiPermissionController {
    @Autowired
    private ApiPermissionService apiPermissionService;

    @GetMapping("/api_permission/page_query")
    public IPage<ApiPermission> pageQuery(ApiPermissionQueryReq req) {
        return apiPermissionService.pageQuery(req);
    }

    @PutMapping("/api_permission/update")
    public ApiPermission updateApiPermission(@RequestBody @Validated ApiPermissionUpdateReq req) {
        return apiPermissionService.updateApiPermission(req);
    }

    @DeleteMapping("/api_permission/del")
    public ApiPermission delApiPermission(@RequestParam("id") Long id) {
        return apiPermissionService.delApiPermission(id);
    }
}
