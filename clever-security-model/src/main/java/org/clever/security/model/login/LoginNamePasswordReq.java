package org.clever.security.model.login;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.security.LoginType;

import javax.validation.constraints.NotBlank;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 14:34 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginNamePasswordReq extends AbstractUserLoginReq {
    /**
     * 用户登录名
     */
    @NotBlank(message = "登录名不能为空")
    private String loginName;
    /**
     * 密码
     */
    @NotBlank(message = "登录密码不能为空")
    private String password;

    @Override
    public LoginType getLoginType() {
        return LoginType.LoginName_Password;
    }
}
