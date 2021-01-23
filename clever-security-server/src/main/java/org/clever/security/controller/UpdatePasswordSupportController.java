package org.clever.security.controller;

import org.clever.security.client.UpdatePasswordSupportClient;
import org.clever.security.dto.request.InitPasswordReq;
import org.clever.security.dto.request.UpdatePasswordReq;
import org.clever.security.dto.response.InitPasswordRes;
import org.clever.security.dto.response.UpdatePasswordRes;
import org.clever.security.service.UpdatePasswordSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/18 20:14 <br/>
 */
@RestController
@RequestMapping("/security/api")
public class UpdatePasswordSupportController implements UpdatePasswordSupportClient {
    @Autowired
    private UpdatePasswordSupportService updatePasswordSupportService;

    /**
     * 设置密码
     */
    @PostMapping("/password/init")
    @Override
    public InitPasswordRes initPassword(@Validated @RequestBody InitPasswordReq req) {
        return updatePasswordSupportService.initPassword(req);
    }

    /**
     * 修改密码
     */
    @PostMapping("/password/update")
    @Override
    public UpdatePasswordRes updatePassword(@Validated @RequestBody UpdatePasswordReq req) {
        return updatePasswordSupportService.updatePassword(req);
    }
}
