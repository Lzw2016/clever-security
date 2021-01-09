package org.clever.security.embed.event;

import lombok.Data;
import org.clever.security.model.UserInfo;

import java.io.Serializable;
import java.util.Set;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/06 21:10 <br/>
 */
@Data
public class AuthorizationSuccessEvent implements Serializable {
    /**
     * 用户信息
     */
    private final UserInfo userInfo;
    /**
     * 角色列表
     */
    private final Set<String> roles;
    /**
     * 权限列表
     */
    private final Set<String> permissions;

    public AuthorizationSuccessEvent(UserInfo userInfo, Set<String> roles, Set<String> permissions) {
        this.userInfo = userInfo;
        this.roles = roles;
        this.permissions = permissions;
    }
}
