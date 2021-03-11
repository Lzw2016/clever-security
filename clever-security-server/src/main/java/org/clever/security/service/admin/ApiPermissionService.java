package org.clever.security.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.dto.request.admin.ApiPermissionQueryReq;
import org.clever.security.dto.request.admin.ApiPermissionUpdateReq;
import org.clever.security.dto.request.admin.DomainQueryReq;
import org.clever.security.dto.response.admin.ApiPermissionQueryRes;
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

    public IPage<ApiPermissionQueryRes> pageQuery(ApiPermissionQueryReq req) {
        req.addOrderFieldMapping("id", "a.id");
        req.addOrderFieldMapping("domainId", "a.domain_id");
        req.addOrderFieldMapping("permissionId", "a.permission_id");
        req.addOrderFieldMapping("title", "a.title");
        req.addOrderFieldMapping("className", "a.class_name");
        req.addOrderFieldMapping("methodName", "a.method_name");
        req.addOrderFieldMapping("methodParams", "a.method_params");
        req.addOrderFieldMapping("apiPath", "a.api_path");
        req.addOrderFieldMapping("apiExist", "a.api_exist");
        req.addOrderFieldMapping("createAt", "a.create_at");
        req.addOrderFieldMapping("updateAt", "a.update_at");
        req.addOrderFieldMapping("strFlag", "b.str_flag");
        req.addOrderFieldMapping("permissionType", "b.permission_type");
        req.addOrderFieldMapping("enabled", "b.enabled");
        req.addOrderFieldMapping("description", "b.description");
        req.addOrderFieldMapping("domainName", "c.name");
        if (req.getOrderFields().isEmpty()) {
            req.addOrderField("createAt", DomainQueryReq.DESC);
        }
        return req.result(apiPermissionMapper.pageQuery(req));
    }

    @Transactional
    public ApiPermission updateApiPermission(ApiPermissionUpdateReq req) {
        ApiPermission apiPermission = BeanMapper.mapper(req, ApiPermission.class);
        int exists = apiPermissionMapper.updateById(apiPermission);
        if (exists <= 0) {
            throw new BusinessException("apiPermission不存在");
        }
        return apiPermissionMapper.selectById(apiPermission.getId());
    }
}
