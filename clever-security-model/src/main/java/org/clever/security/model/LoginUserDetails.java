package org.clever.security.model;

import lombok.Getter;
import lombok.ToString;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.User;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * 登录的用户信息
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-03-16 17:41 <br/>
 */
@ToString(includeFieldNames = false, of = {"username"})
public class LoginUserDetails implements UserDetails, CredentialsContainer {

    /**
     * 主键id
     */
    @Getter
    private final Long id;
    /**
     * 登录名(一条记录的手机号不能当另一条记录的用户名用)
     */
    private final String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 用户类型，0：系统内建，1：外部系统用户
     */
    @Getter
    private final Integer userType;
    /**
     * 手机号
     */
    @Getter
    private final String telephone;
    /**
     * 邮箱
     */
    @Getter
    private final String email;
    /**
     * 帐号过期时间
     */
    @Getter
    private final Date expiredTime;
    /**
     * 帐号锁定标识
     */
    private final boolean locked;
    /**
     * 是否启用
     */
    private final boolean enabled;
    /**
     * 说明
     */
    @Getter
    private String description;
    /**
     * 创建时间
     */
    @Getter
    private Date createAt;
    /**
     * 更新时间
     */
    @Getter
    private Date updateAt;

    /**
     * 凭证过期标识
     */
    private final boolean credentialsNonExpired;
    /**
     * 帐号过期标识
     */
    private final boolean accountNonExpired;
    /**
     * 用户权限信息
     */
    private final Set<UserAuthority> authorities;
    /**
     * 角色信息
     */
    @Getter
    private final Set<String> roles;

    public LoginUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.userType = user.getUserType();
        this.telephone = user.getTelephone();
        this.email = user.getEmail();
        this.expiredTime = user.getExpiredTime();
        this.locked = Objects.equals(user.getLocked(), EnumConstant.User_Locked_0);
        this.enabled = Objects.equals(user.getEnabled(), EnumConstant.User_Enabled_1);
        this.description = user.getDescription();
        this.createAt = user.getCreateAt();
        this.updateAt = user.getUpdateAt();

        this.credentialsNonExpired = true;
        this.accountNonExpired = user.getExpiredTime() == null || user.getExpiredTime().compareTo(new Date()) > 0;
        this.authorities = new HashSet<>();
        this.roles = new HashSet<>();
    }

    @Override
    public void eraseCredentials() {
        this.password = "";
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
        return locked;
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
