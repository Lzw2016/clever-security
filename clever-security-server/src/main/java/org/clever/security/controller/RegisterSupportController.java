package org.clever.security.controller;

import org.clever.security.client.RegisterSupportClient;
import org.clever.security.dto.response.UserRegisterRes;
import org.clever.security.model.register.EmailRegisterReq;
import org.clever.security.model.register.LoginNameRegisterReq;
import org.clever.security.model.register.SmsRegisterReq;
import org.clever.security.service.RegisterSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 17:24 <br/>
 */
@RestController
@RequestMapping("/security/api")
public class RegisterSupportController implements RegisterSupportClient {
    @Autowired
    private RegisterSupportService registerSupportService;

    /**
     * 根据登录名注册
     */
    @PostMapping("/register_by_login_name")
    @Override
    public UserRegisterRes registerByLoginName(@Validated @RequestBody LoginNameRegisterReq req) {
        return registerSupportService.registerByLoginName(req);
    }

    /**
     * 根据邮箱注册
     */
    @PostMapping("/register_by_email")
    @Override
    public UserRegisterRes registerByEmail(@Validated @RequestBody EmailRegisterReq req) {
        return registerSupportService.registerByEmail(req);
    }

    /**
     * 根据短信注册
     */
    @PostMapping("/register_by_sms")
    @Override
    public UserRegisterRes registerBySms(@Validated @RequestBody SmsRegisterReq req) {
        return registerSupportService.registerBySms(req);
    }
}
