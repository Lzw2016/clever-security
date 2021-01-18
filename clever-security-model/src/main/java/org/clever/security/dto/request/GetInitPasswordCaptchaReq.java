package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/18 20:43 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class GetInitPasswordCaptchaReq extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;
    /**
     * 验证码过期时间
     */
    private int effectiveTimeMilli = 60 * 1000;

    public GetInitPasswordCaptchaReq(Long domainId) {
        this.domainId = domainId;
    }
}
