package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 21:56 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class ConfirmLoginScanCodeReq extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;
    /**
     * 扫描二维码
     */
    @NotBlank(message = "扫描登录二维码不能为空")
    private String scanCode;
    /**
     * 绑定的JWT-Token id
     */
    @NotNull(message = "绑定的JWT-Token id不能为null")
    private Long bindTokenId;
    /**
     * 获取登录JWT-Token过期时间(确认登录 -> 获取登录Token时间，默认30秒)
     */
    private int getTokenExpiredTime = 30 * 1000;

    public ConfirmLoginScanCodeReq(Long domainId) {
        this.domainId = domainId;
    }
}
