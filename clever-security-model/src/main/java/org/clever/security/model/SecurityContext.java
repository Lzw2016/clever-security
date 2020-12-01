package org.clever.security.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 21:23 <br/>
 */
@Data
public class SecurityContext implements Serializable {
    // 用户信息

    // 角色信息

    // 权限信息

    /**
     * 权限列表
     */
    private Set<String> permissions = Collections.emptySet();
    ;
    /**
     * 角色列表
     */
    private Set<String> roles = Collections.emptySet();
    ;

    /**
     * 是否拥有指定权限
     */
    public boolean hasPermissions(String... permissions) {
        if (permissions == null || permissions.length <= 0) {
            return true;
        }
        if (this.permissions == null) {
            return false;
        }
        for (String permission : permissions) {
            if (!this.permissions.contains(permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否拥有指定角色
     */
    public boolean hasRoles(String... roles) {
        if (roles == null || roles.length <= 0) {
            return true;
        }
        if (this.roles == null) {
            return false;
        }
        for (String role : roles) {
            if (!this.roles.contains(role)) {
                return false;
            }
        }
        return true;
    }

    public void setPermissions(Set<String> permissions) {
        if (permissions == null) {
            this.permissions = Collections.emptySet();
            return;
        }
        this.permissions = Collections.unmodifiableSet(permissions);
    }

    public void setRoles(Set<String> roles) {
        if (roles == null) {
            this.roles = Collections.emptySet();
            return;
        }
        this.roles = Collections.unmodifiableSet(roles);
    }
}