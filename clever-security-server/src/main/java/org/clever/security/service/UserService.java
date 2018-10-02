package org.clever.security.service;

import org.clever.common.server.service.BaseService;
import org.clever.security.entity.Permission;
import org.clever.security.entity.User;
import org.clever.security.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-17 9:20 <br/>
 */
@Transactional(readOnly = true)
@Service
public class UserService extends BaseService {

    @Autowired
    private UserMapper userMapper;

    public User getUser(String usernameOrTelephone) {
        User user = userMapper.getByUsername(usernameOrTelephone);
        if (user == null) {
            user = userMapper.getByTelephone(usernameOrTelephone);
        }
        return user;
    }

    public List<Permission> findAllPermission(String username, String sysName) {
        return userMapper.findByUsername(username, sysName);
    }

    public Boolean canLogin(String username, String sysName) {
        return userMapper.existsUserBySysName(username, sysName) >= 1;
    }
}
