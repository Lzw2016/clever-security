package org.clever.security.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.SnowFlake;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.dto.request.admin.UiPermissionAddReq;
import org.clever.security.dto.request.admin.UiPermissionQueryReq;
import org.clever.security.dto.request.admin.UiPermissionUpdateReq;
import org.clever.security.dto.response.admin.UiPermissionQueryRes;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.Permission;
import org.clever.security.entity.UiPermission;
import org.clever.security.mapper.PermissionMapper;
import org.clever.security.mapper.UiPermissionMapper;
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
public class UiPermissionService {
    @Autowired
    private UiPermissionMapper uiPermissionMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    public IPage<UiPermissionQueryRes> pageQuery(UiPermissionQueryReq req) {
        return req.result(uiPermissionMapper.pageQuery(req));
    }

//    @Transactional
//    public UiPermission addUiPermission(UiPermissionAddReq req) {
//        String strFlag = PermissionStrFlagUtils.createStrFlag("[ui]");
//        while (permissionMapper.strFlagExist(strFlag) > 0) {
//            strFlag = PermissionStrFlagUtils.createStrFlag("[ui]");
//        }
//        Permission permission = BeanMapper.mapper(req, Permission.class);
//        permission.setId(SnowFlake.SNOW_FLAKE.nextId());
//        permission.setStrFlag(strFlag);
//        permission.setParentId(-1L);
//        permission.setDomainId(req.getDomainId());
//        permission.setResourcesType(EnumConstant.Permission_ResourcesType_3);
//        permissionMapper.insert(permission);
//        UiPermission uiPermission = BeanMapper.mapper(req, UiPermission.class);
//        uiPermission.setPermissionId(permission.getId());
//        uiPermissionMapper.insert(uiPermission);
//        return uiPermissionMapper.selectById(uiPermission.getId());
//    }

    @Transactional
    public UiPermission updateUiPermission(UiPermissionUpdateReq req) {
        UiPermission uiPermission = uiPermissionMapper.getByDomainId(req.getDomainId(), req.getId());
        if (uiPermission == null) {
            throw new BusinessException("该ui组件权限不存在");
        }
        UiPermission updateUiPermission = BeanMapper.mapper(req, UiPermission.class);
        uiPermissionMapper.updateById(updateUiPermission);
        uiPermission = uiPermissionMapper.selectById(updateUiPermission.getId());
        Permission updatePermission = BeanMapper.mapper(req, Permission.class);
        updatePermission.setId(updateUiPermission.getPermissionId());
        permissionMapper.updateById(updatePermission);
        return uiPermission;
    }

    @Transactional
    public UiPermission delUiPermission(Long domainId, Long id) {
        UiPermission uiPermission = uiPermissionMapper.getByDomainId(domainId, id);
        int exists = uiPermissionMapper.deleteById(uiPermission.getId());
        if (exists <= 0) {
            throw new BusinessException("uiPermission不存在");
        }
        permissionMapper.deleteById(uiPermission.getPermissionId());
        return uiPermission;
    }

}
