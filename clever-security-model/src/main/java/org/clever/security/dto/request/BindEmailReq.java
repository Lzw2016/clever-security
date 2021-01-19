package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-16 17:10 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class BindEmailReq extends BaseRequest {
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
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
