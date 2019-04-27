package org.clever.security.token;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;

/**
 * 用户登录请求参数基础Token
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2019-04-27 20:46 <br/>
 */
public abstract class BaseLoginToken implements Authentication, CredentialsContainer, Serializable {

    /**
     * 是否使用记住我功能(当前登录使用的RememberMe功能进行登录的)
     */
    @Setter
    @Getter
    private boolean isRememberMe = false;

    /**
     * 当前登录类型(用户名密码、手机号验证码、其他三方登录方式)
     */
    @Setter
    @Getter
    private String loginType;

    /**
     * 登录请求其他信息(请求IP地址和SessionID,如: WebAuthenticationDetails)
     */
    @Setter
    private Object details;

    /**
     * 登录验证码
     */
    @Setter
    @Getter
    private String captcha;

    /**
     * 登录验证码签名
     */
    @Setter
    @Getter
    private String captchaDigest;

    public BaseLoginToken(String loginType) {
        this.loginType = loginType;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    /**
     * 没有认证成功只能返回false
     */
    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated == this.isAuthenticated()) {
            return;
        }
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
    }

    @Override
    public Object getDetails() {
        return details;
    }

    @Override
    public String getName() {
        if (this.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) this.getPrincipal()).getUsername();
        }
        if (this.getPrincipal() instanceof AuthenticatedPrincipal) {
            return ((AuthenticatedPrincipal) this.getPrincipal()).getName();
        }
        if (this.getPrincipal() instanceof Principal) {
            return ((Principal) this.getPrincipal()).getName();
        }
        return (this.getPrincipal() == null) ? "" : this.getPrincipal().toString();
    }

    @Override
    public void eraseCredentials() {
        eraseSecret(getCredentials());
        eraseSecret(getPrincipal());
        eraseSecret(details);
    }

    private void eraseSecret(Object secret) {
        if (secret instanceof CredentialsContainer) {
            ((CredentialsContainer) secret).eraseCredentials();
        }
    }
}
