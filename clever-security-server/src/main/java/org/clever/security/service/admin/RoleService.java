package org.clever.security.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.RoleQueryReq;
import org.clever.security.dto.response.admin.RoleQueryRes;
import org.clever.security.mapper.RoleMapper;
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

    public IPage<RoleQueryRes> pageQuery(RoleQueryReq req) {
        return req.result(roleMapper.pageQuery(req));
    }
}
