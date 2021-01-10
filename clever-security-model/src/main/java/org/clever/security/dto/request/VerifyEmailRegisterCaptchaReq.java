package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-10 16:36 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VerifyEmailRegisterCaptchaReq extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;
    /**
     * 验证码
     */
    private String captcha;
    /**
     * 验证码签名(校验验证码时需要)
     */
    private String captchaDigest;

    public VerifyEmailRegisterCaptchaReq(Long domainId) {
        this.domainId = domainId;
    }
}
