package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 21:39 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class BindLoginScanCodeReq extends BaseRequest {
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
     * 确认登录过期时间(扫码二维码 -> 确认登录时间，默认30秒)
     */
    private int confirmExpiredTime = 30 * 1000;

    public BindLoginScanCodeReq(Long domainId) {
        this.domainId = domainId;
    }
}
