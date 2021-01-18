package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/01/18 15:03 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class GetBindEmailCaptchaReq extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;
    /**
     * 验证码过期时间
     */
    private int effectiveTimeMilli = 60 * 1000;

    public GetBindEmailCaptchaReq(Long domainId) {
        this.domainId = domainId;
    }
}
