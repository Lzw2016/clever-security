package org.clever.security.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.UserRegisterLogQueryReq;
import org.clever.security.dto.response.admin.UserRegisterLogQueryRes;
import org.clever.security.service.admin.UserRegisterLogService;
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
public class UserRegisterLogController {
    @Autowired
    private UserRegisterLogService userRegisterLogService;

    @GetMapping("/user_register_log/page_query")
    public IPage<UserRegisterLogQueryRes> pageQuery(UserRegisterLogQueryReq req) {
        return userRegisterLogService.pageQuery(req);
    }
}
