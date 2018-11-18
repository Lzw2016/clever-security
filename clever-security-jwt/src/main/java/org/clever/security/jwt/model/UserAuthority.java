package org.clever.security.jwt.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

/**
 * 权限信息
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-03-18 14:05 <br/>
 */
@Getter
public final class UserAuthority implements GrantedAuthority {

    // private final Permission permission;

    private final String authority;
    private final String title;

    public UserAuthority(String authority, String title) {
        this.authority = authority;
        this.title = title;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
