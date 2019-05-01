package org.clever.security.token.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.clever.security.LoginTypeConstant;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-04-27 21:19 <br/>
 */
@ToString
public class UsernamePasswordToken extends BaseLoginToken {

    /**
     * 用户名
     */
    @JsonProperty(value = "principal", access = JsonProperty.Access.WRITE_ONLY)
    @Setter
    @Getter
    private String username;
    /**
     * 密码
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Setter
    @Getter
    private String password;

    public UsernamePasswordToken() {
        super(LoginTypeConstant.UsernamePassword);
    }

    public UsernamePasswordToken(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    /**
     * 擦除密码
     */
    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        password = "";
    }

    /**
     * 返回密码
     */
    @Override
    public Object getCredentials() {
        return password;
    }

    /**
     * 返回用户登录唯一ID
     */
    @Override
    public Object getPrincipal() {
        return username;
    }
}
