package org.clever.security.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.SnowFlake;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.common.utils.tree.BuildTreeUtils;
import org.clever.security.dto.request.admin.UiPermissionAddReq;
import org.clever.security.dto.request.admin.UiPermissionQueryReq;
import org.clever.security.dto.request.admin.UiPermissionUpdateReq;
import org.clever.security.dto.response.admin.MenuAndUiPermissionTreeRes;
import org.clever.security.dto.response.admin.UiPermissionQueryRes;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.MenuPermission;
import org.clever.security.entity.Permission;
import org.clever.security.entity.UiPermission;
import org.clever.security.mapper.MenuPermissionMapper;
import org.clever.security.mapper.PermissionMapper;
import org.clever.security.mapper.UiPermissionMapper;
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
public class UiPermissionService {
    @Autowired
    private UiPermissionMapper uiPermissionMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private MenuPermissionMapper menuPermissionMapper;

    public List<MenuAndUiPermissionTreeRes> menuAndUiTree(Long domainId) {
        List<MenuAndUiPermissionTreeRes> res = uiPermissionMapper.menuAndUiTree(domainId);
        return BuildTreeUtils.buildTree(res);
    }

    public List<UiPermissionQueryRes> findUiByMenu(Long menuId) {
        return uiPermissionMapper.findUiByMenu(menuId);
    }

    public IPage<UiPermissionQueryRes> pageQuery(UiPermissionQueryReq req) {
        return req.result(uiPermissionMapper.pageQuery(req));
    }

    @Transactional
    public UiPermission addUiPermission(UiPermissionAddReq req) {
        MenuPermission menuPermission = menuPermissionMapper.selectById(req.getMenuId());
        if (menuPermission == null) {
            throw new BusinessException("菜单数据不存在");
        }
        String strFlag = PermissionStrFlagUtils.createStrFlag();
        if (permissionMapper.strFlagExist(strFlag) > 0) {
            throw new BusinessException("意外的错误，UI权限字符串重复，请再试一次");
        }
        Permission permission = BeanMapper.mapper(req, Permission.class);
        permission.setId(SnowFlake.SNOW_FLAKE.nextId());
        permission.setDomainId(menuPermission.getDomainId());
        permission.setStrFlag(strFlag);
        permission.setPermissionType(EnumConstant.Permission_PermissionType_3);
        permissionMapper.insert(permission);
        UiPermission uiPermission = BeanMapper.mapper(req, UiPermission.class);
        uiPermission.setId(SnowFlake.SNOW_FLAKE.nextId());
        uiPermission.setDomainId(menuPermission.getDomainId());
        uiPermission.setPermissionId(permission.getId());
        uiPermissionMapper.insert(uiPermission);
        return uiPermissionMapper.selectById(uiPermission.getId());
    }

    @Transactional
    public UiPermission updateUiPermission(UiPermissionUpdateReq req) {
        UiPermission uiPermission = uiPermissionMapper.selectById(req.getId());
        if (uiPermission == null) {
            throw new BusinessException("UI权限数据不存在");
        }
        Permission permission = permissionMapper.selectById(uiPermission.getPermissionId());
        if (permission == null) {
            throw new BusinessException("权限数据不存在");
        }
        UiPermission updateUiPermission = BeanMapper.mapper(req, UiPermission.class);
        uiPermissionMapper.updateById(updateUiPermission);
        Permission updatePermission = BeanMapper.mapper(req, Permission.class);
        updatePermission.setId(permission.getId());
        permissionMapper.updateById(updatePermission);
        return uiPermission;
    }

//    @Transactional
//    public UiPermission delUiPermission(Long domainId, Long id) {
//        UiPermission uiPermission = uiPermissionMapper.getByDomainId(domainId, id);
//        int exists = uiPermissionMapper.deleteById(uiPermission.getId());
//        if (exists <= 0) {
//            throw new BusinessException("uiPermission不存在");
//        }
//        permissionMapper.deleteById(uiPermission.getPermissionId());
//        return uiPermission;
//    }
}
