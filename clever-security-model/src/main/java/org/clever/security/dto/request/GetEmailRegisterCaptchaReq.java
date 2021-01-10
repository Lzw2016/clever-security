package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-10 15:59 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetEmailRegisterCaptchaReq extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;
    /**
     * 验证码过期时间
     */
    private int effectiveTimeMilli = 60 * 1000;

    public GetEmailRegisterCaptchaReq(Long domainId) {
        this.domainId = domainId;
    }
}
