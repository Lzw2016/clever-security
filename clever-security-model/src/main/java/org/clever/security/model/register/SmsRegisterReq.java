package org.clever.security.model.register;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.security.PatternConstant;
import org.clever.security.RegisterType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 14:30 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SmsRegisterReq extends AbstractUserRegisterReq {
    public static final String Telephone_ParamName = "telephone";
    public static final String ValidateCodeDigest_ParamName = "validateCodeDigest";
    public static final String ValidateCode_ParamName = "validateCode";

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = PatternConstant.Telephone_Pattern, message = "手机号格式错误")
    private String telephone;
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
        return RegisterType.Sms_ValidateCode;
    }
}
