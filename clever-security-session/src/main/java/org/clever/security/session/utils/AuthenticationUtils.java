package org.clever.security.session.utils;

import org.clever.security.dto.response.UserRes;
import org.clever.security.entity.User;
import org.clever.security.session.model.LoginUserDetails;
import org.clever.security.session.model.UserLoginToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-17 15:26 <br/>
 */
@SuppressWarnings({"Duplicates", "WeakerAccess"})
public class AuthenticationUtils {

    public static UserLoginToken getUserLoginToken(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        if (authentication instanceof UserLoginToken) {
            return (UserLoginToken) authentication;
        }
        throw new ClassCastException(String.format("类型%1$s无法转换成%2$s类型", authentication.getClass().getName(), UserLoginToken.class.getName()));
    }


    public static LoginUserDetails getLoginUserDetails(UserDetails userDetails) {
        if (userDetails == null) {
            return null;
        }
        if (userDetails instanceof LoginUserDetails) {
            return (LoginUserDetails) userDetails;
        }
        throw new ClassCastException(String.format("类型%1$s无法转换成%2$s类型", userDetails.getClass().getName(), LoginUserDetails.class.getName()));
    }

    public static User getUser(Authentication authentication) {
        UserLoginToken userLoginToken = getUserLoginToken(authentication);
        if (userLoginToken == null) {
            return null;
        }
        LoginUserDetails loginUserDetails = getLoginUserDetails(userLoginToken.getUserDetails());
        if (loginUserDetails == null) {
            return null;
        }
        return loginUserDetails.getUser();
    }

    public static UserRes getUserRes(Authentication authentication) {
        User user = getUser(authentication);
        if (user == null) {
            return null;
        }
        UserRes userRes = new UserRes();
        userRes.setUsername(user.getUsername());
        userRes.setTelephone(user.getTelephone());
        userRes.setEmail(user.getEmail());
        userRes.setUserType(user.getUserType());
        // TODO 读取角色信息
        // userRes.getRoleNames().add();
        // 读取权限信息
        if (authentication.getAuthorities() != null) {
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                userRes.getAuthorities().add(grantedAuthority.getAuthority());
            }
        }
        return userRes;
    }
}
