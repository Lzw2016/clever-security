package org.clever.security.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.common.utils.SnowFlake;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.dto.request.admin.ApiPermissionAddReq;
import org.clever.security.dto.request.admin.ApiPermissionQueryReq;
import org.clever.security.dto.request.admin.ApiPermissionUpdateReq;
import org.clever.security.entity.ApiPermission;
import org.clever.security.mapper.ApiPermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:42 <br/>
 */
@Transactional(readOnly = true)
@Service
public class ApiPermissionService {
    @Autowired
    private ApiPermissionMapper apiPermissionMapper;
    
    public IPage<ApiPermission> pageQuery(ApiPermissionQueryReq req) {
        return req.result(apiPermissionMapper.pageQuery(req));
    }

    @Transactional
    public ApiPermission addUser(ApiPermissionAddReq req) {
        ApiPermission apiPermission = BeanMapper.mapper(req, ApiPermission.class);
        apiPermission.setId(SnowFlake.SNOW_FLAKE.nextId());
        apiPermissionMapper.insert(apiPermission);
        return apiPermissionMapper.selectById(apiPermission.getId());
    }

    @Transactional
    public ApiPermission updateUser(ApiPermissionUpdateReq req) {
        ApiPermission apiPermission = BeanMapper.mapper(req, ApiPermission.class);
        apiPermissionMapper.updateById(apiPermission);
        return apiPermissionMapper.selectById(apiPermission.getId());
    }
}
