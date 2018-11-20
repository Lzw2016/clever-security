package org.clever.security.jwt.model;

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
@Getter
public class UserLoginToken extends AbstractAuthenticationToken {

    public static final String LoginType_Username = "username";
    public static final String LoginType_Telephone = "telephone";

    /**
     * 登录类型取值 “username” “telephone”
     */
    private String loginType = LoginType_Username;
    private String username;
    private String password;
    private String captcha;
    private String captchaDigest;
    @Setter
    private String rememberMe;
    private UserDetails userDetails;

    /**
     * 用于创建登录的凭证信息
     */
    public UserLoginToken(String username, String password, String captcha, String captchaDigest, String rememberMe) {
        super(null);
        this.username = username;
        this.password = password;
        this.captcha = captcha;
        this.captchaDigest = captchaDigest;
        this.rememberMe = rememberMe;
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
        captcha = null;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        if (LoginType_Username.equals(loginType)
                || LoginType_Telephone.equals(loginType)) {
            this.loginType = loginType;
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
