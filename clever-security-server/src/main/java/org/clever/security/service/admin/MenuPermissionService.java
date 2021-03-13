package org.clever.security.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.SnowFlake;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.common.utils.tree.BuildTreeUtils;
import org.clever.security.dto.request.admin.DomainQueryReq;
import org.clever.security.dto.request.admin.MenuPermissionAddReq;
import org.clever.security.dto.request.admin.MenuPermissionQueryReq;
import org.clever.security.dto.request.admin.MenuPermissionUpdateReq;
import org.clever.security.dto.response.admin.MenuPermissionQueryRes;
import org.clever.security.dto.response.admin.MenuPermissionTreeRes;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.MenuPermission;
import org.clever.security.entity.Permission;
import org.clever.security.mapper.DomainMapper;
import org.clever.security.mapper.MenuPermissionMapper;
import org.clever.security.mapper.PermissionMapper;
import org.clever.security.utils.PermissionStrFlagUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:42 <br/>
 */
@Transactional(readOnly = true)
@Service
public class MenuPermissionService {
    @Autowired
    private DomainMapper domainMapper;
    @Autowired
    private MenuPermissionMapper menuPermissionMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    public List<MenuPermissionTreeRes> menuTree(Long domainId) {
        List<MenuPermissionTreeRes> menuTreeList = menuPermissionMapper.menuTree(domainId);
        return BuildTreeUtils.buildTree(menuTreeList);
    }

    public IPage<MenuPermissionQueryRes> pageQuery(MenuPermissionQueryReq req) {
        req.addOrderFieldMapping("id", "a.id");
        req.addOrderFieldMapping("domainId", "a.domain_id");
        req.addOrderFieldMapping("permissionId", "a.permission_id");
        req.addOrderFieldMapping("parentId", "a.parent_id");
        req.addOrderFieldMapping("name", "a.name");
        req.addOrderFieldMapping("icon", "a.icon");
        req.addOrderFieldMapping("path", "a.path");
        req.addOrderFieldMapping("pagePath", "a.page_path");
        req.addOrderFieldMapping("hideMenu", "a.hide_menu");
        req.addOrderFieldMapping("hideChildrenMenu", "a.hide_children_menu");
        req.addOrderFieldMapping("sort", "a.sort");
        req.addOrderFieldMapping("createAt", "a.create_at");
        req.addOrderFieldMapping("updateAt", "a.update_at");
        req.addOrderFieldMapping("strFlag", "b.str_flag");
        req.addOrderFieldMapping("permissionType", "b.permission_type");
        req.addOrderFieldMapping("description", "b.description");
        req.addOrderFieldMapping("enabled", "b.enabled");
        req.addOrderFieldMapping("domainName", "c.name");
        if (req.getOrderFields().isEmpty()) {
            req.addOrderField("createAt", DomainQueryReq.DESC);
        }
        return req.result(menuPermissionMapper.pageQuery(req));
    }

    @Transactional
    public MenuPermission addMenuPermission(MenuPermissionAddReq req) {
        if (domainMapper.exists(req.getDomainId()) <= 0) {
            throw new BusinessException("数据源不存在");
        }
        if (!Objects.equals(req.getParentId(), -1L) && menuPermissionMapper.exists(req.getParentId()) <= 0) {
            throw new BusinessException("上级菜单不存在");
        }
        String strFlag = PermissionStrFlagUtils.createStrFlag();
        if (permissionMapper.strFlagExist(strFlag) > 0) {
            throw new BusinessException("意外的错误，请再试一次");
        }
        Permission permission = BeanMapper.mapper(req, Permission.class);
        permission.setId(SnowFlake.SNOW_FLAKE.nextId());
        permission.setDomainId(req.getDomainId());
        permission.setStrFlag(strFlag);
        permission.setPermissionType(EnumConstant.Permission_PermissionType_2);
        permissionMapper.insert(permission);
        MenuPermission menuPermission = BeanMapper.mapper(req, MenuPermission.class);
        menuPermission.setId(SnowFlake.SNOW_FLAKE.nextId());
        menuPermission.setDomainId(req.getDomainId());
        menuPermission.setPermissionId(permission.getId());
        menuPermission.setParentId(req.getParentId());
        menuPermissionMapper.insert(menuPermission);
        return menuPermissionMapper.selectById(menuPermission.getId());
    }

    @Transactional
    public MenuPermission updateMenuPermission(MenuPermissionUpdateReq req) {
        MenuPermission menuPermission = menuPermissionMapper.selectById(req.getId());
        if (menuPermission == null) {
            throw new BusinessException("菜单数据不存在");
        }
        Permission permission = permissionMapper.selectById(menuPermission.getPermissionId());
        if (permission == null) {
            throw new BusinessException("权限数据不存在");
        }
        MenuPermission updateMenuPermission = BeanMapper.mapper(req, MenuPermission.class);
        updateMenuPermission.setId(menuPermission.getId());
        menuPermissionMapper.updateById(updateMenuPermission);
        Permission updatePermission = BeanMapper.mapper(req, Permission.class);
        updatePermission.setId(permission.getId());
        permissionMapper.updateById(updatePermission);
        return menuPermissionMapper.selectById(req.getId());
    }

//    @Transactional
//    public MenuPermission delUiPermission(Long domainId, Long id) {
//        MenuPermission menuPermission = menuPermissionMapper.getByDomainId(domainId, id);
//        int exists = menuPermissionMapper.deleteById(menuPermission.getId());
//        if (exists <= 0) {
//            throw new BusinessException("uiPermission不存在");
//        }
//        menuPermissionBindMapper.deleteById(menuPermission.getId());
//        permissionMapper.deleteById(menuPermission.getPermissionId());
//        return menuPermission;
//    }
}
