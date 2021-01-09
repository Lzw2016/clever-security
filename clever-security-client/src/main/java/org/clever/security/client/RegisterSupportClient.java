package org.clever.security.client;

import org.clever.security.Constant;
import org.clever.security.client.config.CleverSecurityFeignConfiguration;
import org.clever.security.dto.response.UserRegisterRes;
import org.clever.security.model.register.EmailRegisterReq;
import org.clever.security.model.register.LoginNameRegisterReq;
import org.clever.security.model.register.SmsRegisterReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 17:04 <br/>
 */
@FeignClient(
        contextId = "org.clever.security.client.RegisterSupportClient",
        name = Constant.ServerName,
        path = "/security/api",
        configuration = CleverSecurityFeignConfiguration.class
)
public interface RegisterSupportClient {
    /**
     * 根据登录名注册
     */
    @PostMapping("/register_by_login_name")
    UserRegisterRes registerByLoginName(@Validated @RequestBody LoginNameRegisterReq req);

    /**
     * 根据邮箱注册
     */
    @PostMapping("/register_by_email")
    UserRegisterRes registerByEmail(@Validated @RequestBody EmailRegisterReq req);

    /**
     * 根据短信注册
     */
    @PostMapping("/register_by_sms")
    UserRegisterRes registerBySms(@Validated @RequestBody SmsRegisterReq req);
}
