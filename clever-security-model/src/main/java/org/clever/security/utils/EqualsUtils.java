package org.clever.security.utils;

import org.clever.security.model.SecurityContext;
import org.clever.security.model.UserInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/19 14:01 <br/>
 */
public class EqualsUtils {
    public static boolean equals(SecurityContext a, SecurityContext b) {
        if (a == b) {
            return true;
        }
        if ((a == null) != (b == null)) {
            return false;
        }
        return Objects.equals(toString(a), toString(b));
    }

    protected static String toString(SecurityContext securityContext) {
        if (securityContext == null) {
            return "";
        }
        UserInfo userInfo = securityContext.getUserInfo();
        List<String> roles = new ArrayList<>(securityContext.getRoles());
        List<String> permissions = new ArrayList<>(securityContext.getPermissions());
        StringBuilder sb = new StringBuilder();
        sb.append(userInfo.getUid());
        sb.append(userInfo.getLoginName());
        sb.append(userInfo.getPassword());
        sb.append(userInfo.getTelephone());
        sb.append(userInfo.getEmail());
        sb.append(userInfo.getExpiredTime());
        sb.append(userInfo.getEnabled());
        Collections.sort(roles);
        Collections.sort(permissions);
        roles.forEach(sb::append);
        permissions.forEach(sb::append);
        return sb.toString();
    }
}
