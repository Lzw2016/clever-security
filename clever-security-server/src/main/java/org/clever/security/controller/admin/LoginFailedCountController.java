package org.clever.security.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.LoginFailedCountQueryReq;
import org.clever.security.dto.response.admin.LoginFailedCountQueryRes;
import org.clever.security.entity.LoginFailedCount;
import org.clever.security.service.admin.LoginFailedCountService;
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
public class LoginFailedCountController {
    @Autowired
    private LoginFailedCountService loginFailedCountService;

    @GetMapping("/login_failed_count/page_query")
    public IPage<LoginFailedCountQueryRes> pageQuery(LoginFailedCountQueryReq req) {
        return loginFailedCountService.pageQuery(req);
    }
}
