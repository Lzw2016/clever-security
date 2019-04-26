package org.clever.security.model;

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

    /** 权限字符串 */
    private final String authority;
    /** 权限标题 */
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
