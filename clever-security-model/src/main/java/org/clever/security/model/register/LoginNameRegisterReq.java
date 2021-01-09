package org.clever.security.model.register;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.security.RegisterType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 14:29 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginNameRegisterReq extends AbstractUserRegisterReq {
    public static final String LoginName_ParamName = "loginName";
    public static final String Password_ParamName = "password";

    /**
     * 用户登录名(允许修改)
     * ^[a-zA-Z0-9\u4e00-\u9fa5()\[\]{}_-]{4,16}$
     */
    @NotBlank(message = "用户登录名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{4,32}$", message = "用户名只能由“字母、数字、下划线、中划线”组成，且长度在4~32个字符范围内")
    private String loginName;
    /**
     * 密码
     */
    @NotBlank(message = "登录密码不能为空")
    private String password;

    @Override
    public RegisterType getRegisterType() {
        return RegisterType.LoginName_Password;
    }
}
