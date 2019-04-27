package org.clever.security.token;

import lombok.Getter;
import lombok.ToString;
import org.clever.security.LoginTypeConstant;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-04-27 21:19 <br/>
 */
@ToString
public class UsernamePasswordToken extends BaseLoginToken {

    @Getter
    private String username;
    @Getter
    private String password;


    public UsernamePasswordToken() {
        super(LoginTypeConstant.UsernamePassword);
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
