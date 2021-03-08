package org.clever.security.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.SnowFlake;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.dto.request.admin.MenuPermissionAddReq;
import org.clever.security.dto.request.admin.MenuPermissionQueryReq;
import org.clever.security.dto.request.admin.MenuPermissionUpdateReq;
import org.clever.security.dto.response.admin.MenuPermissionQueryRes;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.MenuPermission;
import org.clever.security.entity.Permission;
import org.clever.security.mapper.MenuPermissionMapper;
import org.clever.security.mapper.PermissionMapper;
import org.clever.security.utils.PermissionStrFlagUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:42 <br/>
 */
@Transactional(readOnly = true)
@Service
public class MenuPermissionService {
    @Autowired
    private MenuPermissionMapper menuPermissionMapper;
//    @Autowired
//    private MenuPermissionBindMapper menuPermissionBindMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    public IPage<MenuPermissionQueryRes> pageQuery(MenuPermissionQueryReq req) {
        return req.result(menuPermissionMapper.pageQuery(req));
    }

//    @Transactional
//    public MenuPermission addUiPermission(MenuPermissionAddReq req) {
//        MenuPermission exist = menuPermissionMapper.getByDomainId(req.getDomainId(), req.getParentId());
//        if (exist == null && req.getParentId() != -1) {
//            throw new BusinessException("上级菜单id不存在");
//        }
//        String strFlag = PermissionStrFlagUtils.createStrFlag("[menu]");
//        while (permissionMapper.strFlagExist(strFlag) > 0) {
//            strFlag = PermissionStrFlagUtils.createStrFlag("[menu]");
//        }
//        Permission permission = BeanMapper.mapper(req, Permission.class);
//        permission.setId(SnowFlake.SNOW_FLAKE.nextId());
//        permission.setStrFlag(strFlag);
//        permission.setParentId(-1L);
//        permission.setDomainId(req.getDomainId());
//        permission.setResourcesType(EnumConstant.Permission_ResourcesType_2);
//        permissionMapper.insert(permission);
//        MenuPermission menuPermission = BeanMapper.mapper(req, MenuPermission.class);
//        menuPermission.setPermissionId(permission.getId());
//        menuPermissionMapper.insert(menuPermission);
//        return menuPermissionMapper.selectById(menuPermission.getId());
//    }

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
