package org.clever.security.client;

import org.clever.security.Constant;
import org.clever.security.client.config.CleverSecurityFeignConfiguration;
import org.clever.security.dto.request.InitPasswordReq;
import org.clever.security.dto.request.UpdatePasswordReq;
import org.clever.security.dto.response.InitPasswordRes;
import org.clever.security.dto.response.UpdatePasswordRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/18 20:03 <br/>
 */
@FeignClient(
        contextId = "org.clever.security.client.ResetPasswordSupportClient",
        name = Constant.ServerName,
        path = "/security/api",
        configuration = CleverSecurityFeignConfiguration.class
)
public interface UpdatePasswordSupportClient {
    /**
     * 设置密码
     */
    @PostMapping("/password/init")
    InitPasswordRes initPassword(@Validated @RequestBody InitPasswordReq req);

    /**
     * 修改密码
     */
    @PostMapping("/password/update")
    UpdatePasswordRes updatePassword(@Validated @RequestBody UpdatePasswordReq req);
}
