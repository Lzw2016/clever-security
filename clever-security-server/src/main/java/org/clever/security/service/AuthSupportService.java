package org.clever.security.service;

import org.clever.security.client.AuthSupportClient;
import org.clever.security.dto.request.CacheContextReq;
import org.clever.security.dto.request.GetApiPermissionReq;
import org.clever.security.dto.request.LoadContextReq;
import org.clever.security.dto.response.GetApiPermissionRes;
import org.clever.security.mapper.ApiPermissionMapper;
import org.clever.security.model.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/18 22:23 <br/>
 */
@Transactional(readOnly = true)
@Primary
@Service
public class AuthSupportService implements AuthSupportClient {
    @Autowired
    private ApiPermissionMapper apiPermissionMapper;


    @Override
    public GetApiPermissionRes getApiPermission(GetApiPermissionReq req) {
        return apiPermissionMapper.getByTargetMethod(req.getDomainId(), req.getClassName(), req.getMethodName(), req.getMethodParams());
    }

    @Override
    public void cacheContext(CacheContextReq req) {

    }

    @Override
    public SecurityContext loadContext(LoadContextReq req) {
        return null;
    }
}
