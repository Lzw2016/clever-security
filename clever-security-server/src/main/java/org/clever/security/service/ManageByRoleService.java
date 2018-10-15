package org.clever.security.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.dto.request.RoleQueryPageReq;
import org.clever.security.dto.request.RoleUpdateReq;
import org.clever.security.dto.response.RoleInfoRes;
import org.clever.security.entity.Role;
import org.clever.security.mapper.PermissionMapper;
import org.clever.security.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-02 23:52 <br/>
 */
@Transactional(readOnly = true)
@Service
@Slf4j
public class ManageByRoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    public IPage<Role> findByPage(RoleQueryPageReq roleQueryPageReq) {
        Page<Role> page = new Page<>(roleQueryPageReq.getPageNo(), roleQueryPageReq.getPageSize());
        page.setRecords(roleMapper.findByPage(roleQueryPageReq, page));
        return page;
    }

    @Transactional
    public Role addRole(Role role) {
        Role oldRole = roleMapper.getByName(role.getName());
        if (oldRole != null) {
            throw new BusinessException("角色[" + role.getName() + "]已经存在");
        }
        roleMapper.insert(role);
        return roleMapper.selectById(role.getId());
    }

    @Transactional
    public Role updateRole(String name, RoleUpdateReq roleUpdateReq) {
        Role oldRole = roleMapper.getByName(name);
        if (oldRole == null) {
            throw new BusinessException("角色[" + roleUpdateReq.getName() + "]不存在");
        }
        Role update = new Role();
        update.setId(oldRole.getId());
        if (StringUtils.isNotBlank(roleUpdateReq.getName()) && !Objects.equals(roleUpdateReq.getName(), oldRole.getName())) {
            update.setName(roleUpdateReq.getName());
        }
        update.setDescription(roleUpdateReq.getDescription());
        roleMapper.updateById(update);
        if (update.getName() != null) {
            roleMapper.updateUserRoleByRoleName(oldRole.getName(), roleUpdateReq.getName());
            roleMapper.updateRolePermissionByRoleName(oldRole.getName(), roleUpdateReq.getName());
            //TODO 更新受影响用户的Session
        }
        return roleMapper.getByName(roleUpdateReq.getName());
    }

    @Transactional
    public Role delRole(String name) {
        // TODO 删除权限
        return null;
    }

    public RoleInfoRes getRoleInfo(String name) {
        Role role = roleMapper.getByName(name);
        if (role == null) {
            return null;
        }
        RoleInfoRes roleInfoRes = BeanMapper.mapper(role, RoleInfoRes.class);
        roleInfoRes.setPermissionList(permissionMapper.findByRoleName(role.getName()));
        return roleInfoRes;
    }
}
