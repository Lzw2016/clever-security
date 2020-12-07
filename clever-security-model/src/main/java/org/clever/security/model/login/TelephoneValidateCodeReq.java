package org.clever.security.model.login;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.security.LoginType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 14:39 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TelephoneValidateCodeReq extends AbstractUserLoginReq {
    public static final String Telephone_ParamName = "telephone";
    public static final String ValidateCode_ParamName = "validateCode";
    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "(?:0|86|\\+86)?1[3456789]\\d{9}", message = "手机号格式错误")
    private String telephone;
    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String validateCode;

    @Override
    public LoginType getLoginType() {
        return LoginType.Telephone_ValidateCode;
    }
}
