package org.clever.security.utils;

import org.clever.security.dto.response.UserRes;
import org.clever.security.model.LoginUserDetails;
import org.clever.security.token.SecurityContextToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-17 15:26 <br/>
 */
public class AuthenticationUtils {

    public static SecurityContextToken getSecurityContextToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getSecurityContextToken(authentication);
    }

    public static SecurityContextToken getSecurityContextToken(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        if (!(authentication instanceof SecurityContextToken)) {
            throw new ClassCastException(String.format("Authentication类型错误,%s | %s", authentication.getClass(), authentication));
        }
        return (SecurityContextToken) authentication;
    }

    public static UserRes getUserRes() {
        SecurityContextToken securityContextToken = getSecurityContextToken();
        return getUserRes(securityContextToken);
    }

    public static UserRes getUserRes(Authentication authentication) {
        SecurityContextToken securityContextToken = getSecurityContextToken(authentication);
        return getUserRes(securityContextToken);
    }

    public static UserRes getUserRes(SecurityContextToken securityContextToken) {
        if (securityContextToken == null) {
            return null;
        }
        LoginUserDetails loginUserDetails = securityContextToken.getUserDetails();
        if (loginUserDetails == null) {
            return null;
        }
        UserRes userRes = new UserRes();
        userRes.setUsername(loginUserDetails.getUsername());
        userRes.setTelephone(loginUserDetails.getTelephone());
        userRes.setEmail(loginUserDetails.getEmail());
        userRes.setUserType(loginUserDetails.getUserType());
        // 读取角色信息
        userRes.getRoleNames().addAll(loginUserDetails.getRoles());
        // 读取权限信息
        if (loginUserDetails.getAuthorities() != null) {
            for (GrantedAuthority grantedAuthority : loginUserDetails.getAuthorities()) {
                userRes.getAuthorities().add(grantedAuthority.getAuthority());
            }
        }
        return userRes;
    }
}
