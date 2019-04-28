package org.clever.security.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.ToString;
import org.clever.security.model.LoginUserDetails;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * 登录成功的Token
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2019-04-27 20:50 <br/>
 */
@ToString(exclude = {"userDetails"})
public class SecurityContextToken extends AbstractAuthenticationToken {

    /**
     * 用户登录请求参数Token
     */
    @JsonIgnore
    @Getter
    private BaseLoginToken loginToken;
    /**
     * 数据库中用户信息
     */
    @JsonIgnore
    @Getter
    private LoginUserDetails userDetails;

    /**
     * 用于创建登录成功的用户信息
     */
    public SecurityContextToken(BaseLoginToken loginToken, LoginUserDetails userDetails) {
        super(userDetails.getAuthorities());
        this.loginToken = loginToken;
        this.userDetails = userDetails;
        super.setAuthenticated(true);
    }

    /**
     * 获取登录凭证(password 密码)
     */
    @Override
    public Object getCredentials() {
        return loginToken;
    }

    /**
     * 获取登录的用户信息
     */
    @Override
    public Object getPrincipal() {
        return userDetails;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated == this.isAuthenticated()) {
            return;
        }
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        loginToken.eraseCredentials();
        userDetails.eraseCredentials();
    }
}
