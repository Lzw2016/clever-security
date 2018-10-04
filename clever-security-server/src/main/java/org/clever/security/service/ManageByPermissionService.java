package org.clever.security.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.exception.BusinessException;
import org.clever.security.dto.request.PermissionUpdateReq;
import org.clever.security.dto.request.PermissionQueryReq;
import org.clever.security.entity.Permission;
import org.clever.security.entity.model.WebPermissionModel;
import org.clever.security.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-03 12:51 <br/>
 */
@Transactional(readOnly = true)
@Service
@Slf4j
public class ManageByPermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    public IPage<WebPermissionModel> findByPage(PermissionQueryReq queryReq) {
        Page<WebPermissionModel> page = new Page<>(queryReq.getPageNo(), queryReq.getPageSize());
        page.setRecords(permissionMapper.findByPage(queryReq, page));
        return page;
    }

    @Transactional
    public WebPermissionModel addPermission(Permission permission) {
        int count = permissionMapper.existsPermission(permission.getPermissionStr());
        if (count > 0) {
            throw new BusinessException("权限已存在");
        }
        permissionMapper.insert(permission);
        return permissionMapper.getByPermissionStr(permission.getPermissionStr());
    }

    @Transactional
    public WebPermissionModel updatePermission(PermissionUpdateReq permissionUpdateReq) {
        // TODO 更新权限
        return null;
    }

    public WebPermissionModel getPermissionModel(String permissionStr) {
        return permissionMapper.getByPermissionStr(permissionStr);
    }

    public WebPermissionModel delPermissionModel(String permissionStr) {
        return null;
    }
}
