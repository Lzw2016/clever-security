package org.clever.security.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 登录的用户信息(用户名密码)<br/>
 * 作者： lzw<br/>
 * 创建时间：2018-03-15 12:58 <br/>
 */
@ToString(exclude = {"userDetails"})
public class UserLoginToken extends AbstractAuthenticationToken {

    @Getter
    private String username;
    @Getter
    private String password;
    @Getter
    @Setter
    private boolean isRememberMe = false;
    private UserDetails userDetails;

    /**
     * 用于创建登录的凭证信息
     */
    public UserLoginToken(String username, String password) {
        super(null);
        this.username = username;
        this.password = password;
        setAuthenticated(false);
    }

    /**
     * 用于创建登录成功的用户信息
     */
    public UserLoginToken(UserDetails userDetails) {
        super(userDetails.getAuthorities());
        this.username = userDetails.getUsername();
        this.password = userDetails.getPassword();
        this.userDetails = userDetails;
        super.setAuthenticated(true);
    }

    /**
     * password 密码
     */
    @Override
    public Object getCredentials() {
        return password;
    }

    /**
     * UserDetails 用户信息
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
        password = null;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
