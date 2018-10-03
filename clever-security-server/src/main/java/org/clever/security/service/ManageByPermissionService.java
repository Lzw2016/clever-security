package org.clever.security.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.clever.security.dto.request.RolePermissionQueryReq;
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

    public IPage<WebPermissionModel> findByPage(RolePermissionQueryReq queryReq) {
        Page<WebPermissionModel> page = new Page<>(queryReq.getPageNo(), queryReq.getPageSize());
        page.setRecords(permissionMapper.findByPage(queryReq, page));
        return page;
    }

}
