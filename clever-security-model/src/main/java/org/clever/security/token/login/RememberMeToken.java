package org.clever.security.token.login;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.clever.security.LoginTypeConstant;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-04-28 18:25 <br/>
 */
public class RememberMeToken extends BaseLoginToken {

    /**
     * 用户名
     */
    @JsonIgnore
    @Setter
    @Getter
    private String username;

    public RememberMeToken() {
        super(LoginTypeConstant.RememberMe);
    }

    /**
     * 返回密码
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * 返回用户登录唯一ID
     */
    @Override
    public Object getPrincipal() {
        return username;
    }
}
