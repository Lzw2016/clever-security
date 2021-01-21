package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.request.BaseRequest;
import org.clever.security.PatternConstant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-16 17:10 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class BindSmsReq extends BaseRequest {
    @NotBlank(message = "绑定的手机号不能为空")
    @Pattern(regexp = PatternConstant.Telephone_Pattern, message = "手机号格式错误")
    private String newTelephone;
    /**
     * 账号密码
     */
    @NotBlank(message = "密码不能为空")
    private String passWord;
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
}
