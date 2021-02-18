package org.clever.security.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.ValidateCodeQueryReq;
import org.clever.security.dto.response.admin.ValidateCodeQueryRes;
import org.clever.security.service.admin.ValidateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/18 14:04 <br/>
 */
@RestController
@RequestMapping("/security/admin/api")
public class ValidateCodeController {
    @Autowired
    private ValidateCodeService validateCodeService;

    @GetMapping("/validate_code/page_query")
    public IPage<ValidateCodeQueryRes> pageQuery(ValidateCodeQueryReq req) {
        return validateCodeService.pageQuery(req);
    }
}
