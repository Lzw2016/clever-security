package org.clever.security.client;

import org.clever.security.Constant;
import org.clever.security.client.config.CleverSecurityFeignConfiguration;
import org.clever.security.dto.request.GetLoginCaptchaReq;
import org.clever.security.dto.response.GetLoginCaptchaRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/12 22:03 <br/>
 */
@FeignClient(
        contextId = "org.clever.security.client.LoginSupportClient",
        name = Constant.ServerName,
        path = "/security/api",
        configuration = CleverSecurityFeignConfiguration.class
)
public interface LoginSupportClient {
    /**
     * 获取登录图片验证码
     */
    @GetMapping("/login_captcha")
    GetLoginCaptchaRes getLoginCaptcha(GetLoginCaptchaReq req);


}
