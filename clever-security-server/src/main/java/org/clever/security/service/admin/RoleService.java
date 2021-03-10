package org.clever.security.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.SnowFlake;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.dto.request.admin.DomainQueryReq;
import org.clever.security.dto.request.admin.RoleAddReq;
import org.clever.security.dto.request.admin.RoleQueryReq;
import org.clever.security.dto.request.admin.RoleUpdateReq;
import org.clever.security.dto.response.admin.RoleQueryRes;
import org.clever.security.entity.Role;
import org.clever.security.mapper.DomainMapper;
import org.clever.security.mapper.RoleMapper;
import org.clever.security.mapper.RolePermissionMapper;
import org.clever.security.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:42 <br/>
 */
@Transactional(readOnly = true)
@Service
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private DomainMapper domainMapper;

    public IPage<RoleQueryRes> pageQuery(RoleQueryReq req) {
        req.addOrderFieldMapping("id", "a.id");
        req.addOrderFieldMapping("domainId", "a.domain_id");
        req.addOrderFieldMapping("name", "a.name");
        req.addOrderFieldMapping("enabled", "a.enabled");
        req.addOrderFieldMapping("description", "a.description");
        req.addOrderFieldMapping("createAt", "a.create_at");
        req.addOrderFieldMapping("updateAt", "a.update_at");
        req.addOrderFieldMapping("domainName", "b.name");
        if (req.getOrderFields().isEmpty()) {
            req.addOrderField("createAt", DomainQueryReq.DESC);
        }
        return req.result(roleMapper.pageQuery(req));
    }

    @Transactional
    public Role addRole(RoleAddReq req) {
        Role role = BeanMapper.mapper(req, Role.class);
        role.setId(SnowFlake.SNOW_FLAKE.nextId());
        int exists = domainMapper.exists(role.getDomainId());
        if (exists <= 0) {
            throw new BusinessException("数据域不存在");
        }
        roleMapper.insert(role);
        return roleMapper.selectById(role.getId());
    }

    @Transactional
    public Role updateRole(RoleUpdateReq req) {
        Role role = roleMapper.getByDomainId(req.getDomainId(), req.getId());
        if (role == null) {
            throw new BusinessException("该域角色不存在");
        }
        Role update = BeanMapper.mapper(req, Role.class);
        roleMapper.updateById(update);
        return roleMapper.selectById(update.getId());
    }

    @Transactional
    public Role delRole(Long domainId, Long id) {
        Role role = roleMapper.getByDomainId(domainId, id);
        int exists = roleMapper.deleteById(role.getId());
        if (exists <= 0) {
            throw new BusinessException("apiPermission不存在");
        }
        //删除role权限数据
        userRoleMapper.deleteByRoleId(role.getId());
        //删除用户-role
        rolePermissionMapper.deleteByRoleId(role.getId());
        // TODO 删除角色关联数据
        return role;
    }
}
