package org.clever.security.service;

import org.clever.common.utils.SnowFlake;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.common.utils.tuples.TupleTow;
import org.clever.security.client.AuthSupportClient;
import org.clever.security.dto.request.CacheContextReq;
import org.clever.security.dto.request.GetApiPermissionReq;
import org.clever.security.dto.request.LoadContextReq;
import org.clever.security.dto.request.RegisterApiPermissionReq;
import org.clever.security.dto.response.GetApiPermissionRes;
import org.clever.security.dto.response.RegisterApiPermissionRes;
import org.clever.security.entity.User;
import org.clever.security.entity.UserSecurityContext;
import org.clever.security.mapper.*;
import org.clever.security.model.SecurityContext;
import org.clever.security.utils.ConvertUtils;
import org.clever.security.utils.EqualsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/18 22:23 <br/>
 */
@Transactional
@Primary
@Service
public class AuthSupportService implements AuthSupportClient {
    @Autowired
    private ApiPermissionMapper apiPermissionMapper;
    @Autowired
    private UserSecurityContextMapper userSecurityContextMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public GetApiPermissionRes getApiPermission(GetApiPermissionReq req) {
        return apiPermissionMapper.getByTargetMethod(req.getDomainId(), req.getClassName(), req.getMethodName(), req.getMethodParams());
    }

    @Override
    public void cacheContext(CacheContextReq req) {
        TupleTow<UserSecurityContext, SecurityContext> tupleTow = getSecurityContext(req.getDomainId(), req.getUid());
        if (tupleTow == null) {
            userSecurityContextMapper.deleteByUid(req.getDomainId(), req.getUid());
            return;
        }
        UserSecurityContext userSecurityContext = userSecurityContextMapper.getByUid(req.getDomainId(), req.getUid());
        // 不存在直接新增
        if (userSecurityContext == null) {
            userSecurityContextMapper.insert(tupleTow.getValue1());
            return;
        }
        SecurityContext securityContext = JacksonMapper.getInstance().fromJson(userSecurityContext.getSecurityContext(), SecurityContext.class);
        // 存在则比对刷新
        if (EqualsUtils.equals(securityContext, tupleTow.getValue2())) {
            return;
        }
        userSecurityContextMapper.updateSecurityContextById(userSecurityContext.getId(), JacksonMapper.getInstance().toJson(tupleTow.getValue2()));
    }

    @Override
    public SecurityContext loadContext(LoadContextReq req) {
        UserSecurityContext userSecurityContext = userSecurityContextMapper.getByUid(req.getDomainId(), req.getUid());
        if (userSecurityContext == null) {
            TupleTow<UserSecurityContext, SecurityContext> tupleTow = getSecurityContext(req.getDomainId(), req.getUid());
            if (tupleTow == null) {
                return null;
            }
            userSecurityContextMapper.insert(tupleTow.getValue1());
            return tupleTow.getValue2();
        }
        return JacksonMapper.getInstance().fromJson(userSecurityContext.getSecurityContext(), SecurityContext.class);
    }

    @Override
    public RegisterApiPermissionRes registerApiPermission(RegisterApiPermissionReq req) {
        return null;
    }

    /**
     * 刷新 SecurityContext
     *
     * @param domainId 域id
     * @param uid      用户id
     */
    public TupleTow<UserSecurityContext, SecurityContext> getSecurityContext(Long domainId, String uid) {
        User user = userMapper.getByUid(uid);
        if (user == null) {
            return null;
        }
        // SecurityContext
        SecurityContext securityContext = new SecurityContext(ConvertUtils.convertToUserInfo(user));
        securityContext.setRoles(roleMapper.findRolesByUid(domainId, uid));
        securityContext.setPermissions(permissionMapper.findPermissionByUid(domainId, uid));
        // UserSecurityContext
        UserSecurityContext userSecurityContext = new UserSecurityContext();
        userSecurityContext.setId(SnowFlake.SNOW_FLAKE.nextId());
        userSecurityContext.setDomainId(domainId);
        userSecurityContext.setUid(uid);
        userSecurityContext.setSecurityContext(JacksonMapper.getInstance().toJson(securityContext));
        return TupleTow.creat(userSecurityContext, securityContext);
    }
}
