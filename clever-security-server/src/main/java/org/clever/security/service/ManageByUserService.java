package org.clever.security.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.dto.request.UserAddReq;
import org.clever.security.dto.request.UserQueryPageReq;
import org.clever.security.dto.request.UserUpdateReq;
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
//    @Autowired
//    private QueryMapper queryMapper;

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
    public User addUser(UserAddReq userAddReq) {
        User user = BeanMapper.mapper(userAddReq, User.class);
        final Date now = new Date();
        if (user.getExpiredTime() != null && now.compareTo(user.getExpiredTime()) >= 0) {
            throw new BusinessException("设置过期时间小于当前时间");
        }
        if (userMapper.existsByUserName(user.getUsername()) > 0) {
            throw new BusinessException("用户名已经存在");
        }
        if (StringUtils.isNotBlank(user.getTelephone()) && userMapper.existsByTelephone(user.getTelephone()) > 0) {
            throw new BusinessException("手机号已经被绑定");
        }
        if (StringUtils.isNotBlank(user.getEmail()) && userMapper.existsByEmail(user.getEmail()) > 0) {
            throw new BusinessException("邮箱已经被绑定");
        }
        // 登录名(一条记录的手机号不能当另一条记录的用户名用)
        if (StringUtils.isNotBlank(user.getTelephone()) && userMapper.existsByUserName(user.getTelephone()) > 0) {
            // 手机号不能是已存在用户的登录名
            throw new BusinessException("手机号已经被绑定");
        }
        if (user.getUsername().matches("1[0-9]{10}") && userMapper.existsByTelephone(user.getUsername()) > 0) {
            // 登录名符合手机号格式，而且该手机号已经存在
            throw new BusinessException("手机号已经被绑定");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
        if (userAddReq.getSysNameList() != null) {
            for (String sysName : userAddReq.getSysNameList()) {
                userMapper.addUserSys(user.getUsername(), sysName);
            }
        }
        return userMapper.selectById(user.getId());
    }

    @Transactional
    public User updateUser(UserUpdateReq req) {
        // TODO 更新用户信息
        return null;
    }

    @Transactional
    public User deleteUser(String username) {
        // TODO 删除用户
        return null;
    }
}
