package org.clever.security.model;

import lombok.Data;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 21:23 <br/>
 */
@Data
public class SecurityContext implements Serializable {
    /**
     * 用户信息
     */
    private final UserInfo userInfo;
    /**
     * 角色列表
     */
    private Set<String> roles = Collections.emptySet();
    /**
     * 权限列表
     */
    private Set<String> permissions = Collections.emptySet();

    public SecurityContext(UserInfo userInfo) {
        Assert.notNull(userInfo, "参数userInfo不能为null");
        this.userInfo = userInfo;
    }

    public SecurityContext(UserInfo userInfo, Set<String> roles, Set<String> permissions) {
        this(userInfo);
        this.setRoles(roles);
        this.setPermissions(permissions);
    }

    /**
     * 是否拥有指定全部角色
     */
    public boolean hasRoles(String... roles) {
        return hasAll(this.roles, roles);
    }

    /**
     * 是否拥有指定全部权限
     */
    public boolean hasPermissions(String... permissions) {
        return hasAll(this.permissions, permissions);
    }

    /**
     * 是否拥有指定任意角色
     */
    public boolean hasAnyRoles(String... roles) {
        return hasAny(this.roles, roles);
    }

    /**
     * 是否拥有指定任意权限
     */
    public boolean hasAnyPermissions(String... permissions) {
        return hasAny(this.permissions, permissions);
    }

    protected boolean hasAll(Set<String> source, String... target) {
        if (target == null || target.length <= 0) {
            return true;
        }
        if (source == null || source.isEmpty()) {
            return false;
        }
        boolean flag = true;
        for (String str : target) {
            if (!source.contains(str)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    protected boolean hasAny(Set<String> source, String... target) {
        if (target == null || target.length <= 0) {
            return true;
        }
        if (source == null || source.isEmpty()) {
            return false;
        }
        boolean flag = false;
        for (String str : target) {
            if (source.contains(str)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    protected void setPermissions(Set<String> permissions) {
        if (permissions == null) {
            this.permissions = Collections.emptySet();
            return;
        }
        this.permissions = Collections.unmodifiableSet(permissions);
    }

    protected void setRoles(Set<String> roles) {
        if (roles == null) {
            this.roles = Collections.emptySet();
            return;
        }
        this.roles = Collections.unmodifiableSet(roles);
    }
}