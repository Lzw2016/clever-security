package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 作者：ymx <br/>
 * 创建时间：2020/12/28 16:00 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WriteBackScanCodeLoginReq extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;
    /**
     * 浏览器扫描码
     */
    @NotBlank(message = "二维码不能为空")
    private String scanCode;

    /**
     * 扫描二维码状态
     */
    @NotNull(message = "扫描二维码状态不能为空")
    private Integer scanCodeState;

    /**
     * 登录时间
     */
    @NotNull(message = "登录时间不能为空")
    private Date loginTime;

    /**
     * 扫描二维码失效原因
     */
    private String invalidReason;

    /**
     * 登录生成的JWT-Token id
     */
    private Long tokenId;

    public WriteBackScanCodeLoginReq(Long domainId) {
        this.domainId = domainId;
    }
}
