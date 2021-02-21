package org.clever.security.controller.admin;

import org.clever.security.service.admin.UserDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/15 22:58 <br/>
 */
@RestController
@RequestMapping("/security/admin/api")
public class UserDomainController {
    @Autowired
    private UserDomainService userDomainService;

    @DeleteMapping("/user_domain/del")
    public Boolean delUserDomain(@RequestParam("domainId") Long domainId, @RequestParam("uid") String uid) {
        return userDomainService.delUserDomain(domainId, uid);
    }
}
