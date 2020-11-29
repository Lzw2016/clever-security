package org.clever.security.model.login;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.security.LoginType;

import javax.validation.constraints.NotBlank;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 14:42 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EmailValidateCodeReq extends AbstractUserLoginReq {
    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    private String email;
    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String validateCode;

    @Override
    public LoginType getLoginType() {
        return LoginType.Email_ValidateCode;
    }
}
