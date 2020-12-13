package org.clever.security.client;

import org.clever.security.Constant;
import org.clever.security.client.config.CleverSecurityFeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/12 22:03 <br/>
 */
@FeignClient(
        contextId = "org.clever.security.client.LoginSupportClient",
        name = Constant.ServerName,
        path = "/api/manage",
        configuration = CleverSecurityFeignConfiguration.class
)
public interface LoginSupportClient {

//    @GetMapping("/")
//     verifyLoginCaptcha();
}
