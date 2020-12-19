package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 13:25 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetLoginCaptchaReq extends BaseRequest {
    @NotBlank(message = "域id不能为空")
    private Long domainId;
    /**
     * 验证码过期时间
     */
    private int effectiveTimeMilli = 60 * 1000;

    public GetLoginCaptchaReq(Long domainId) {
        this.domainId = domainId;
    }
}
