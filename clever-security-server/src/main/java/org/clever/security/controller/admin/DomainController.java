package org.clever.security.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.DomainQueryReq;
import org.clever.security.entity.Domain;
import org.clever.security.service.admin.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/15 22:58 <br/>
 */
@RestController
@RequestMapping("/security/admin/api")
public class DomainController {
    @Autowired
    private DomainService domainService;

    @GetMapping("/domain/page_query")
    public IPage<Domain> pageQuery(DomainQueryReq req) {
        return domainService.pageQuery(req);
    }
}
