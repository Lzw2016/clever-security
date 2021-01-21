package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.request.BaseRequest;
import org.clever.security.PatternConstant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-10 20:06 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class VerifySmsValidateCodeReq extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;
    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String code;
    /**
     * 验证码签名(校验验证码时需要)
     */
    @NotBlank(message = "验证码签名不能为空")
    private String codeDigest;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = PatternConstant.Telephone_Pattern, message = "手机号格式错误")
    private String telephone;

    public VerifySmsValidateCodeReq(Long domainId) {
        this.domainId = domainId;
    }
}
