package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 21:39 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BindLoginScanCodeReq extends BaseRequest {
    @NotBlank(message = "域id不能为空")
    private Long domainId;
    /**
     * 扫描二维码
     */
    @NotBlank(message = "扫描登录二维码不能为空")
    private String scanCode;
    /**
     * 用户id
     */
    @NotBlank(message = "用户id不能为空")
    private String uid;

    public BindLoginScanCodeReq() {
    }

    public BindLoginScanCodeReq(Long domainId) {
        this.domainId = domainId;
    }
}
