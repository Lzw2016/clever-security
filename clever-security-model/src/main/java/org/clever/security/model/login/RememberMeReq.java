package org.clever.security.model.login;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.security.LoginType;

import javax.validation.constraints.NotBlank;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 15:21 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RememberMeReq extends AbstractUserLoginReq {
    public static final String RememberMeToken_ParamName = "rememberMeToken";
    /**
     * “记住我”功能的token
     */
    @NotBlank(message = "RememberMeToken不能为空")
    private String rememberMeToken;

    @Override
    public LoginType getLoginType() {
        return LoginType.RememberMe;
    }
}
