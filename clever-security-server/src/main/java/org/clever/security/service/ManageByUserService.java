package org.clever.security.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.codec.CryptoUtils;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.config.GlobalConfig;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-02 20:53 <br/>
 */
@Transactional(readOnly = true)
@Service
@Slf4j
public class ManageByUserService {

    @Autowired
    private GlobalConfig globalConfig;
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
        // 密码先解密再加密
        byte[] passwordData = EncodeDecodeUtils.decodeBase64(user.getPassword());
        byte[] key = EncodeDecodeUtils.decodeHex(globalConfig.getPasswordAesKey());
        byte[] iv = EncodeDecodeUtils.decodeHex(globalConfig.getPasswordAesIv());
        user.setPassword(CryptoUtils.aesDecrypt(passwordData, key, iv));
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
    public User updateUser(String username, UserUpdateReq req) {
        User oldUser = userMapper.getByUsername(username);
        if (oldUser == null) {
            throw new BusinessException("用户不存在");
        }
        // 修改了密码
        if (StringUtils.isNotBlank(req.getPassword())) {
            // 密码先解密再加密
            byte[] passwordData = EncodeDecodeUtils.decodeBase64(req.getPassword());
            byte[] key = EncodeDecodeUtils.decodeHex(globalConfig.getPasswordAesKey());
            byte[] iv = EncodeDecodeUtils.decodeHex(globalConfig.getPasswordAesIv());
            req.setPassword(CryptoUtils.aesDecrypt(passwordData, key, iv));
            req.setPassword(bCryptPasswordEncoder.encode(req.getPassword()));
        } else {
            req.setPassword(null);
        }
        // 修改了手机号
        if (StringUtils.isNotBlank(req.getTelephone()) && !req.getTelephone().equals(oldUser.getTelephone())) {
            if (userMapper.existsByTelephone(req.getTelephone()) > 0 || userMapper.existsByUserName(req.getTelephone()) > 0) {
                throw new BusinessException("手机号已经被绑定");
            }
        } else {
            req.setTelephone(null);
        }
        // 修改了邮箱
        if (StringUtils.isNotBlank(req.getEmail()) && !req.getEmail().equals(oldUser.getEmail())) {
            if (userMapper.existsByEmail(req.getEmail()) > 0) {
                throw new BusinessException("邮箱已经被绑定");
            }
        } else {
            req.setEmail(null);
        }
        // 设置了过期
        if (req.getExpiredTime() != null && req.getExpiredTime().compareTo(new Date()) <= 0) {
            // TODO 1.删除Session 2.失效remember_me_token
        }
        User user = BeanMapper.mapper(req, User.class);
        user.setId(oldUser.getId());
        userMapper.updateById(user);
        user = userMapper.selectById(oldUser.getId());
        // 更新关联系统
        if (req.getSysNameList() == null) {
            req.setSysNameList(new HashSet<>());
        }
        // 获取关联系统列表
        List<String> sysNameList = userMapper.findSysNameByUsername(user.getUsername());
        Set<String> addSysName = new HashSet<>(req.getSysNameList());
        addSysName.removeAll(sysNameList);
        Set<String> delSysName = new HashSet<>(sysNameList);
        delSysName.removeAll(req.getSysNameList());
        // 新增
        for (String sysName : addSysName) {
            userMapper.addUserSys(user.getUsername(), sysName);
        }
        // 删除
        for (String sysName : delSysName) {
            userMapper.delUserSys(user.getUsername(), sysName);
            // TODO 删除Session
        }
        // TODO 1.更新Session 2.失效remember_me_token
        return user;
    }

    @Transactional
    public User deleteUser(String username) {
        User user = userMapper.getByUsername(username);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        // TODO 1.删除用户信息 2.删除关联数据 user_role、user_sys 3.失效remember_me_token、Session
        return user;
    }
}
