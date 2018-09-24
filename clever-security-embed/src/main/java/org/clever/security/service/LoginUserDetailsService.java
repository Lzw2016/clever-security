package org.clever.security.service;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.UserClient;
import org.clever.security.entity.Permission;
import org.clever.security.entity.User;
import org.clever.security.model.LoginUserDetails;
import org.clever.security.model.UserAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-16 10:54 <br/>
 */
@Transactional(readOnly = true)
@Service
@Slf4j
public class LoginUserDetailsService implements UserDetailsService {

    @Autowired
    private UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("### 开始加载用户信息 [usernameOrTelephone={}]", username);
        // 从本地数据库查询
        User user = userClient.getUser(username);
        if (user == null) {
            log.info("### 用户不存在 [usernameOrTelephone={}]", username);
            throw new UsernameNotFoundException("用户不存在，usernameOrTelephone=" + username);
        }
        // 获取用户所有权限
        List<Permission> permissionList = userClient.findAllPermission(user.getUsername());
        // 获取用所有权限
        LoginUserDetails userDetails = new LoginUserDetails(user);
        for (Permission permission : permissionList) {
            userDetails.getAuthorities().add(new UserAuthority(permission));
        }
        return userDetails;
    }
}
