package org.clever.security.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.clever.security.dto.request.RoleQueryPageReq;
import org.clever.security.entity.Role;
import org.clever.security.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public IPage<Role> findByPage(RoleQueryPageReq roleQueryPageReq) {
        Page<Role> page = new Page<>(roleQueryPageReq.getPageNo(), roleQueryPageReq.getPageSize());
        page.setRecords(roleMapper.findByPage(roleQueryPageReq, page));
        return page;
    }

}
