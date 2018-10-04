package org.clever.security.service;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.RoleBindPermissionRes;
import org.clever.security.dto.response.UserBindRoleRes;
import org.clever.security.dto.response.UserBindSysRes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-03 21:24 <br/>
 */
@Transactional(readOnly = true)
@Service
@Slf4j
public class ManageBySecurityService {

    @Transactional
    public List<UserBindSysRes> userBindSys(UserBindSysReq userBindSysReq) {
        // TODO 用户系统绑定
        return null;
    }

    @Transactional
    public List<UserBindRoleRes> userBindRole(UserBindRoleReq userBindSysReq) {
        // TODO 用户角色绑定
        return null;
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
