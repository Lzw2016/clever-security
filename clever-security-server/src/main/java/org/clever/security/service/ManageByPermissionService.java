package org.clever.security.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.IDCreateUtils;
import org.clever.security.dto.request.PermissionQueryReq;
import org.clever.security.dto.request.PermissionUpdateReq;
import org.clever.security.entity.Permission;
import org.clever.security.entity.model.WebPermissionModel;
import org.clever.security.mapper.PermissionMapper;
import org.clever.security.mapper.WebPermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    @Autowired
    private WebPermissionMapper webPermissionMapper;

    public IPage<WebPermissionModel> findByPage(PermissionQueryReq queryReq) {
        Page<WebPermissionModel> page = new Page<>(queryReq.getPageNo(), queryReq.getPageSize());
        page.setRecords(permissionMapper.findByPage(queryReq, page));
        return page;
    }

    @Transactional
    public WebPermissionModel addPermission(Permission permission) {
        if (StringUtils.isBlank(permission.getPermissionStr())) {
            permission.setPermissionStr(IDCreateUtils.shortUuid());
        }
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

    @Transactional
    public WebPermissionModel delPermissionModel(String permissionStr) {
        WebPermissionModel webPermissionModel = permissionMapper.getByPermissionStr(permissionStr);
        if (webPermissionModel == null) {
            throw new BusinessException("权限[" + permissionStr + "]不存在");
        }
        permissionMapper.deleteById(webPermissionModel.getPermissionId());
        webPermissionMapper.deleteById(webPermissionModel.getWebPermissionId());
        permissionMapper.delRolePermission(permissionStr);
        // TODO 计算影响的用户 更新Session
        return webPermissionModel;
    }

    @Transactional
    public List<WebPermissionModel> delPermissionModels(Set<String> permissionSet) {
        List<WebPermissionModel> list = new ArrayList<>();
        for (String permission : permissionSet) {
            WebPermissionModel webPermissionModel = permissionMapper.getByPermissionStr(permission);
            if (webPermissionModel == null) {
                throw new BusinessException("权限[" + permission + "]不存在");
            }
            list.add(webPermissionModel);
        }
        for (WebPermissionModel webPermissionModel : list) {
            permissionMapper.deleteById(webPermissionModel.getPermissionId());
            webPermissionMapper.deleteById(webPermissionModel.getWebPermissionId());
            permissionMapper.delRolePermission(webPermissionModel.getPermissionStr());
        }
        // TODO 计算影响的用户 更新Session
        return list;
    }
}
