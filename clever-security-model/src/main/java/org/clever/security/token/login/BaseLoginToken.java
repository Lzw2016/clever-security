package org.clever.security.token.login;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(value = {"isRememberMe", "credentials", "authorities"})
public abstract class BaseLoginToken implements Authentication, CredentialsContainer, Serializable {

    /**
     * 是否使用记住我功能
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

    /**
     * 设置当前Token是否认证成功(只能设置false)
     */
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated == this.isAuthenticated()) {
            return;
        }
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
    }

    /**
     * 返回登录请求其他信息
     */
    @Override
    public Object getDetails() {
        return details;
    }

    /**
     * 返回认证用户的唯一ID
     */
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

    /**
     * 擦除密码
     */
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
