package org.clever.security.model;

import lombok.Getter;
import org.clever.security.entity.Permission;
import org.springframework.security.core.GrantedAuthority;

/**
 * 权限信息
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-03-18 14:05 <br/>
 */
@Getter
public final class UserAuthority implements GrantedAuthority {

    private final Permission permission;

    public UserAuthority(Permission permission) {
        this.permission = permission;
    }

    @Override
    public String getAuthority() {
        return permission == null ? null : permission.getPermissionStr();
    }
}
