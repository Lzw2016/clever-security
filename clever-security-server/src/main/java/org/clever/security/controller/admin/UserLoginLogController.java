package org.clever.security.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.UserLoginLogQueryReq;
import org.clever.security.dto.response.admin.UserLoginLogQueryRes;
import org.clever.security.service.admin.UserLoginLogService;
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
public class UserLoginLogController {
    @Autowired
    private UserLoginLogService userLoginLogService;

    @GetMapping("/user_login_log/page_query")
    public IPage<UserLoginLogQueryRes> pageQuery(UserLoginLogQueryReq req) {
        return userLoginLogService.pageQuery(req);
    }
}
