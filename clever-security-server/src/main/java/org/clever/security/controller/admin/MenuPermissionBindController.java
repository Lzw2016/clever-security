package org.clever.security.controller.admin;

import org.clever.security.service.admin.ApiPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:42 <br/>
 */
@RestController
@RequestMapping("/security/admin/api")
public class MenuPermissionBindController {
    @Autowired
    private ApiPermissionService apiPermissionService;

}
