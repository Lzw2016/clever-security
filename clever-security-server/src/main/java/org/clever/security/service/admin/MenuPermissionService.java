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
import org.clever.security.entity.MenuPermission;
import org.clever.security.entity.Permission;
import org.clever.security.mapper.MenuPermissionMapper;
import org.clever.security.mapper.PermissionMapper;
import org.clever.security.utils.PermissionStrFlagUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:42 <br/>
 */
@Transactional(readOnly = true)
@Service
public class MenuPermissionService {
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
        MenuPermission exist = menuPermissionMapper.getByDomainId(req.getDomainId(), req.getParentId());
        if (exist == null && req.getParentId() != -1) {
            throw new BusinessException("上级菜单id不存在");
        }
        String strFlag = PermissionStrFlagUtils.createStrFlag();
        while (permissionMapper.strFlagExist(strFlag) > 0) {
            strFlag = PermissionStrFlagUtils.createStrFlag();
        }
        Permission permission = BeanMapper.mapper(req, Permission.class);
        permission.setId(SnowFlake.SNOW_FLAKE.nextId());
        permission.setStrFlag(strFlag);
//        permission.setParentId(-1L);
        permission.setDomainId(req.getDomainId());
//        permission.setResourcesType(EnumConstant.Permission_ResourcesType_2);
        permissionMapper.insert(permission);
        MenuPermission menuPermission = BeanMapper.mapper(req, MenuPermission.class);
        menuPermission.setPermissionId(permission.getId());
        menuPermissionMapper.insert(menuPermission);
        return menuPermissionMapper.selectById(menuPermission.getId());
    }

    @Transactional
    public MenuPermission updateUiPermission(MenuPermissionUpdateReq req) {
        MenuPermission menuPermission = menuPermissionMapper.getByDomainId(req.getDomainId(), req.getId());
        if (menuPermission == null) {
            throw new BusinessException("该ui组件权限不存在");
        }
        MenuPermission exist = menuPermissionMapper.getByDomainId(req.getDomainId(), req.getParentId());
        if (exist == null && req.getParentId() != -1) {
            throw new BusinessException("上级菜单id不存在");
        }
        MenuPermission updateMenuPermission = BeanMapper.mapper(req, MenuPermission.class);
        menuPermissionMapper.updateById(updateMenuPermission);
        updateMenuPermission = menuPermissionMapper.selectById(updateMenuPermission.getId());
        Permission updatePermission = BeanMapper.mapper(req, Permission.class);
        updatePermission.setId(updateMenuPermission.getPermissionId());
        permissionMapper.updateById(updatePermission);
        return updateMenuPermission;
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
