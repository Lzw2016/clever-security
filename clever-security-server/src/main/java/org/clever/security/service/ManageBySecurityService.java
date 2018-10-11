package org.clever.security.service;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.exception.BusinessException;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.RoleBindPermissionRes;
import org.clever.security.dto.response.UserBindRoleRes;
import org.clever.security.dto.response.UserBindSysRes;
import org.clever.security.entity.Role;
import org.clever.security.mapper.UserMapper;
import org.clever.security.service.internal.UserBindRoleService;
import org.clever.security.service.internal.UserBindSysNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-03 21:24 <br/>
 */
@Transactional(readOnly = true)
@Service
@Slf4j
public class ManageBySecurityService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserBindSysNameService userBindSysNameService;
    @Autowired
    private UserBindRoleService userBindRoleService;

    @Transactional
    public List<UserBindSysRes> userBindSys(UserBindSysReq userBindSysReq) {
        // 校验用户时候存在
        for (String username : userBindSysReq.getUsernameList()) {
            int count = userMapper.existsByUserName(username);
            if (count <= 0) {
                throw new BusinessException("用户[" + username + "]不存在");
            }
        }
        List<UserBindSysRes> result = new ArrayList<>();
        // 设置用户绑定的系统
        for (String username : userBindSysReq.getUsernameList()) {
            userBindSysNameService.resetUserBindSys(username, userBindSysReq.getSysNameList());
            List<String> sysNameList = userMapper.findSysNameByUsername(username);
            UserBindSysRes userBindSysRes = new UserBindSysRes();
            userBindSysRes.setUsername(username);
            userBindSysRes.setSysNameList(sysNameList);
            result.add(userBindSysRes);
        }
        return result;
    }

    @Transactional
    public List<UserBindRoleRes> userBindRole(UserBindRoleReq userBindSysReq) {
        // 校验用户时候存在
        for (String username : userBindSysReq.getUsernameList()) {
            int count = userMapper.existsByUserName(username);
            if (count <= 0) {
                throw new BusinessException("用户[" + username + "]不存在");
            }
        }
        List<UserBindRoleRes> result = new ArrayList<>();
        // 设置用户绑定的系统
        for (String username : userBindSysReq.getUsernameList()) {
            userBindRoleService.resetUserBindRole(username, userBindSysReq.getRoleNameList());
            List<Role> roleList = userMapper.findRoleByUsername(username);
            UserBindRoleRes userBindRoleRes = new UserBindRoleRes();
            userBindRoleRes.setUsername(username);
            userBindRoleRes.setRoleList(roleList);
            result.add(userBindRoleRes);
        }
        return result;
    }

    @Transactional
    public List<RoleBindPermissionRes> roleBindPermission(RoleBindPermissionReq roleBindPermissionReq) {
        // TODO 角色权限绑定
        return null;
    }

    @Transactional
    public UserBindSysRes userBindSys(UserSysReq userSysReq) {
        // TODO 为用户添加登录的系统
        return null;
    }

    @Transactional
    public UserBindSysRes userUnBindSys(UserSysReq userSysReq) {
        // TODO 为用户删除登录的系统
        return null;
    }

    @Transactional
    public UserBindRoleRes userBindRole(UserRoleReq userRoleReq) {
        // TODO 为用户添加角色
        return null;
    }

    @Transactional
    public UserBindRoleRes userUnBindRole(UserRoleReq userRoleReq) {
        // TODO 为用户删除角色
        return null;
    }

    @Transactional
    public RoleBindPermissionRes roleBindPermission(RolePermissionReq rolePermissionReq) {
        // TODO 为角色添加权限
        return null;
    }

    @Transactional
    public RoleBindPermissionRes roleUnBindPermission(RolePermissionReq rolePermissionReq) {
        // TODO 为角色删除权限
        return null;
    }
}
