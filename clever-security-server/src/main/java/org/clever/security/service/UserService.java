package org.clever.security.service;

import org.clever.common.exception.BusinessException;
import org.clever.common.server.service.BaseService;
import org.clever.security.entity.Permission;
import org.clever.security.entity.User;
import org.clever.security.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-17 9:20 <br/>
 */
@Transactional(readOnly = true)
@Service
public class UserService extends BaseService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserMapper userMapper;

    @Transactional
    public User addUser(User user) {
        final Date now = new Date();
        if (user.getExpiredTime() != null && now.compareTo(user.getExpiredTime()) >= 0) {
            throw new BusinessException("设置过期时间小于当前时间");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
        return userMapper.selectById(user.getId());
    }

    public User getUser(String usernameOrTelephone) {
        User user = userMapper.getByUsername(usernameOrTelephone);
        if (user == null) {
            user = userMapper.getByTelephone(usernameOrTelephone);
        }
        return user;
    }

    public List<Permission> findAllPermission(String username) {
        return userMapper.findByUsername(username);
    }
}
