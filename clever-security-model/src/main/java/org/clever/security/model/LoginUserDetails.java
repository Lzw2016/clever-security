package org.clever.security.model;

import lombok.ToString;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.User;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * 登录用户信息
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-03-16 17:41 <br/>
 */
@ToString(includeFieldNames = false, of = {"username"})
public class LoginUserDetails implements UserDetails, CredentialsContainer {

    private final String username;
    private String password;
    /**
     * 帐号锁定标识
     */
    private final boolean accountNonLocked;
    /**
     * 凭证过期标识
     */
    private final boolean credentialsNonExpired;
    /**
     * 帐号过期标识
     */
    private final boolean accountNonExpired;
    /**
     * 是否启用
     */
    private final boolean enabled;
    /**
     * 原始用户信息
     */
    private User user;
    /**
     * 用户权限信息
     */
    private final Set<UserAuthority> authorities;

    // TODO 角色信息
    // private final Set

    public LoginUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.accountNonExpired = user.getExpiredTime() == null || user.getExpiredTime().compareTo(new Date()) > 0;
        this.accountNonLocked = Objects.equals(user.getLocked(), EnumConstant.User_Locked_0);
        this.credentialsNonExpired = true;
        this.enabled = Objects.equals(user.getEnabled(), EnumConstant.User_Enabled_1);
        this.user = user;
        this.authorities = new HashSet<>();
    }

    public User getUser() {
        return user;
    }

    @Override
    public void eraseCredentials() {
        this.password = "";
        user.setPassword("");
    }

    @Override
    public Collection<UserAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public int hashCode() {
        if (username == null) {
            return super.hashCode();
        }
        return username.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof LoginUserDetails)) {
            return false;
        }
        LoginUserDetails objTmp = (LoginUserDetails) obj;
        if (this.username == null) {
            return objTmp.username == null;
        }
        return this.username.equals(objTmp.username);
    }
}
