package org.clever.security.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.dto.request.UserQueryPageReq;
import org.clever.security.dto.response.UserInfoRes;
import org.clever.security.entity.User;
import org.clever.security.mapper.PermissionMapper;
import org.clever.security.mapper.RoleMapper;
import org.clever.security.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-02 20:53 <br/>
 */
@Transactional(readOnly = true)
@Service
@Slf4j
public class ManageByUserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    public IPage<User> findByPage(UserQueryPageReq userQueryPageReq) {
        Page<User> page = new Page<>(userQueryPageReq.getPageNo(), userQueryPageReq.getPageSize());
        page.setRecords(userMapper.findByPage(userQueryPageReq, page));
        return page;
    }

    public UserInfoRes getUserInfo(String username) {
        User user = userMapper.getByUsername(username);
        if (user == null) {
            return null;
        }
        UserInfoRes userInfoRes = BeanMapper.mapper(user, UserInfoRes.class);
        userInfoRes.setRoleList(roleMapper.findByUsername(user.getUsername()));
        userInfoRes.setPermissionList(permissionMapper.findByUsername(user.getUsername()));
        userInfoRes.setSysNameList(userMapper.findSysNameByUsername(user.getUsername()));
        return userInfoRes;
    }

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

}
