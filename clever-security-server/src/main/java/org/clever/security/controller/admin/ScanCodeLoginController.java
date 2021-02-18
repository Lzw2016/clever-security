package org.clever.security.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.ScanCodeLoginQueryReq;
import org.clever.security.dto.response.admin.ScanCodeLoginDetailRes;
import org.clever.security.dto.response.admin.ScanCodeLoginQueryRes;
import org.clever.security.service.admin.ScanCodeLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/17 20:25 <br/>
 */
@RestController
@RequestMapping("/security/admin/api")
public class ScanCodeLoginController {
    @Autowired
    private ScanCodeLoginService scanCodeLoginService;

    @GetMapping("/scan_code_login/page_query")
    public IPage<ScanCodeLoginQueryRes> pageQuery(ScanCodeLoginQueryReq req) {
        return scanCodeLoginService.pageQuery(req);
    }

    @GetMapping("/scan_code_login/detail")
    public ScanCodeLoginDetailRes detailScanCodeLogin(@RequestParam("id") Long id) {
        return scanCodeLoginService.detailScanCodeLogin(id);
    }

}
