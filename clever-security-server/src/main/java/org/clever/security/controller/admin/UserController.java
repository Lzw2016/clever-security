package org.clever.security.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.UserAddReq;
import org.clever.security.dto.request.admin.UserQueryReq;
import org.clever.security.dto.request.admin.UserUpdateReq;
import org.clever.security.dto.response.admin.UserQueryRes;
import org.clever.security.entity.User;
import org.clever.security.service.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/15 22:58 <br/>
 */
@RestController
@RequestMapping("/security/admin/api")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/page_query")
    public IPage<UserQueryRes> pageQuery(UserQueryReq req) {
        return userService.pageQuery(req);
    }

    @PostMapping("/user/add")
    public User addDomain(@RequestBody @Validated UserAddReq req) {
        return userService.addUser(req);
    }

    @PutMapping("/user/update")
    public User updateDomain(@RequestBody @Validated UserUpdateReq req) {
        return userService.updateUser(req);
    }
}
