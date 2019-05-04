package org.clever.security.service.local;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.security.client.UserClient;
import org.clever.security.dto.request.UserAuthenticationReq;
import org.clever.security.dto.response.UserAuthenticationRes;
import org.clever.security.entity.Permission;
import org.clever.security.entity.User;
import org.clever.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-11 19:29 <br/>
 */
@Component
@Slf4j
public class UserServiceProxy implements UserClient {
    @Autowired
    private UserService userService;

    @Override
    public User getUser(String unique) {
        return userService.getUser(unique);
    }

    @Override
    public List<Permission> findAllPermission(String username, String sysName) {
        return userService.findAllPermission(username, sysName);
    }

    @Override
    public Boolean canLogin(String username, String sysName) {
        return userService.canLogin(username, sysName);
    }

    @Override
    public UserAuthenticationRes authentication(UserAuthenticationReq req) {
        return userService.authenticationAndRes(req);
    }
}
