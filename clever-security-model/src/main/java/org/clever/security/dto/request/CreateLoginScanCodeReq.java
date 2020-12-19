package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 21:15 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CreateLoginScanCodeReq extends BaseRequest {
    @NotBlank(message = "域id不能为空")
    private Long domainId;
    /**
     * 扫码登录二维码有效时间(生成二维码 -> 扫码请求时间，默认60秒)
     */
    private int expiredTime = 60 * 1000;
    /**
     * 确认登录过期时间(扫码二维码 -> 确认登录时间，默认30秒)
     */
    private int confirmExpiredTime = 30 * 1000;
    /**
     * 获取登录JWT-Token过期时间(确认登录 -> 获取登录Token时间，默认30秒)
     */
    private int getTokenExpiredTime = 30 * 1000;

    public CreateLoginScanCodeReq(Long domainId) {
        this.domainId = domainId;
    }
}
