package org.clever.security.model.register;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.security.RegisterType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 14:30 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EmailRegisterReq extends AbstractUserRegisterReq {
    public static final String Email_ParamName = "email";
    public static final String ValidateCodeDigest_ParamName = "validateCodeDigest";
    public static final String ValidateCode_ParamName = "validateCode";

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    private String email;
    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String validateCode;
    /**
     * 验证码签名
     */
    @NotBlank(message = "验证码签名不能为空")
    private String validateCodeDigest;

    @Override
    public RegisterType getRegisterType() {
        return RegisterType.Email_ValidateCode;
    }
}
