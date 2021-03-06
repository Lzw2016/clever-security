package org.clever.security.service;

import org.clever.common.exception.BusinessException;
import org.clever.common.utils.SnowFlake;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.common.utils.tuples.TupleTow;
import org.clever.security.client.AuthSupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.GetAllApiPermissionRes;
import org.clever.security.dto.response.GetApiPermissionRes;
import org.clever.security.dto.response.RegisterApiPermissionRes;
import org.clever.security.entity.*;
import org.clever.security.mapper.*;
import org.clever.security.model.SecurityContext;
import org.clever.security.model.auth.ApiPermissionEntity;
import org.clever.security.model.auth.ApiPermissionModel;
import org.clever.security.utils.ConvertUtils;
import org.clever.security.utils.EqualsUtils;
import org.clever.security.utils.PermissionStrFlagUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/18 22:23 <br/>
 */
@Transactional
@Primary
@Service
public class AuthSupportService implements AuthSupportClient {
    @Autowired
    private UserSecurityContextMapper userSecurityContextMapper;
    @Autowired
    private DomainMapper domainMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private ApiPermissionMapper apiPermissionMapper;

    @Override
    public GetApiPermissionRes getApiPermission(GetApiPermissionReq req) {
        return apiPermissionMapper.getByTargetMethod(req.getDomainId(), req.getClassName(), req.getMethodName(), req.getMethodParams());
    }

    @Override
    public void cacheContext(CacheContextReq req) {
        TupleTow<UserSecurityContext, SecurityContext> tupleTow = getSecurityContext(req.getDomainId(), req.getUid());
        if (tupleTow == null) {
            userSecurityContextMapper.deleteByDomainIdAndUid(req.getDomainId(), req.getUid());
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
    public GetAllApiPermissionRes getAllApiPermission(GetAllApiPermissionReq req) {
        Domain domain = domainMapper.selectById(req.getDomainId());
        if (domain == null) {
            throw new BusinessException("权限域不存在,domainId=" + req.getDomainId());
        }
        GetAllApiPermissionRes res = new GetAllApiPermissionRes();
        res.setAllApiPermissionList(apiPermissionMapper.findApiPermissionBydDomainId(req.getDomainId()));
        return res;
    }

    @Override
    public RegisterApiPermissionRes registerApiPermission(RegisterApiPermissionReq req) {
        Domain domain = domainMapper.selectById(req.getDomainId());
        if (domain == null) {
            throw new BusinessException("权限域不存在,domainId=" + req.getDomainId());
        }
        List<ApiPermissionModel> apiApiPermissionList = req.getApiPermissionList();
        // 排序
        Collections.sort(apiApiPermissionList);
        // 加载当前模块所有的权限信息 - moduleAllPermission
        List<ApiPermissionEntity> moduleAllPermission = apiPermissionMapper.findApiPermissionBydDomainId(req.getDomainId());
        // 保存系统中有而数据库里没有的资源 - addPermission
        List<ApiPermissionModel> addPermission = addApiPermission(req.getDomainId(), apiApiPermissionList, moduleAllPermission);
        // 统计数据库中有而系统中没有的资源数据 - notExistPermission
        moduleAllPermission = apiPermissionMapper.findApiPermissionBydDomainId(req.getDomainId());
        List<ApiPermissionModel> notExistPermissionList = getNotExistPermission(apiApiPermissionList, moduleAllPermission);
        // 构造返回信息
        RegisterApiPermissionRes res = new RegisterApiPermissionRes();
        res.setAddPermissionList(addPermission);
        res.setNotExistPermissionList(notExistPermissionList);
        return res;
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
        SecurityContext securityContext = new SecurityContext(
                ConvertUtils.convertToUserInfo(user),
                roleMapper.findRolesByUid(domainId, uid),
                permissionMapper.findPermissionByUid(domainId, uid)
        );
        // UserSecurityContext
        UserSecurityContext userSecurityContext = new UserSecurityContext();
        userSecurityContext.setId(SnowFlake.SNOW_FLAKE.nextId());
        userSecurityContext.setDomainId(domainId);
        userSecurityContext.setUid(uid);
        userSecurityContext.setSecurityContext(JacksonMapper.getInstance().toJson(securityContext));
        return TupleTow.creat(userSecurityContext, securityContext);
    }

    /**
     * 保存API权限
     */
    public void saveApiPermission(Long domainId, ApiPermissionModel apiPermissionModel) {
        Permission permission = BeanMapper.mapper(apiPermissionModel, Permission.class);
        permission.setId(SnowFlake.SNOW_FLAKE.nextId());
        permission.setDomainId(domainId);
        permission.setPermissionType(EnumConstant.Permission_PermissionType_1);
        ApiPermission apiPermission = BeanMapper.mapper(apiPermissionModel, ApiPermission.class);
        apiPermission.setId(SnowFlake.SNOW_FLAKE.nextId());
        apiPermission.setDomainId(domainId);
        apiPermission.setPermissionId(permission.getId());
        apiPermission.setApiExist(EnumConstant.ApiPermission_ApiExist_1);
        permissionMapper.insert(permission);
        apiPermissionMapper.insert(apiPermission);
    }

    /**
     * 保存所有新增的权限 (module targetClass targetMethod resourcesUrl)
     *
     * @param domainId             权限域Id
     * @param apiApiPermissionList 当前模块所有权限
     * @param moduleAllPermission  数据库中当前模块所有的权限信息
     * @return 新增的权限
     */
    protected List<ApiPermissionModel> addApiPermission(Long domainId, List<ApiPermissionModel> apiApiPermissionList, List<ApiPermissionEntity> moduleAllPermission) {
        List<ApiPermissionModel> addPermission = new ArrayList<>();
        Set<String> setPermission = new HashSet<>();
        // targetClass targetMethod targetMethodParams
        String format = "%1$-255s|%2$-255s|%3$-255s";
        for (ApiPermissionEntity permission : moduleAllPermission) {
            setPermission.add(String.format(format, permission.getClassName(), permission.getMethodName(), permission.getMethodParams()));
        }
        for (ApiPermissionModel permission : apiApiPermissionList) {
            String key = String.format(format, permission.getClassName(), permission.getMethodName(), permission.getMethodParams());
            if (setPermission.contains(key)) {
                continue;
            }
            // 新增不存在的权限配置
            saveApiPermission(domainId, permission);
            addPermission.add(permission);
        }
        return addPermission;
    }

    /**
     * 统计数据库中有而系统中没有的资源数据
     *
     * @param apiApiPermissionList 当前模块所有权限
     * @param moduleAllPermission  数据库中当前模块所有的权限信息
     * @return 数据库中有而系统中没有的资源数据
     */
    protected List<ApiPermissionModel> getNotExistPermission(List<ApiPermissionModel> apiApiPermissionList, List<ApiPermissionEntity> moduleAllPermission) {
        List<ApiPermissionModel> notExistPermission = new ArrayList<>();
        Map<String, ApiPermissionModel> permissionMap = new HashMap<>();
        // targetClass targetMethod targetMethodParams
        String format = "%1$-255s|%2$-255s|%3$-255s";
        for (ApiPermissionModel permission : apiApiPermissionList) {
            permissionMap.put(String.format(format, permission.getClassName(), permission.getMethodName(), permission.getMethodParams()), permission);
        }
        for (ApiPermissionEntity dbPermission : moduleAllPermission) {
            String key = String.format(format, dbPermission.getClassName(), dbPermission.getMethodName(), dbPermission.getMethodParams());
            ApiPermissionModel permission = permissionMap.get(key);
            if (permission == null) {
                // 数据库里有,系统中没有 - 更新 apiExist
                if (Objects.equals(dbPermission.getApiExist(), EnumConstant.ApiPermission_ApiExist_1)) {
                    ApiPermission update = new ApiPermission();
                    update.setId(dbPermission.getApiPermissionId());
                    update.setApiExist(EnumConstant.ApiPermission_ApiExist_0);
                    apiPermissionMapper.updateById(update);
                }
                notExistPermission.add(ConvertUtils.convertToApiPermissionModel(dbPermission));
                continue;
            }
            // 数据库里有,系统中也有 - 更新
            boolean updatePermissionFlag = false;
            boolean updateApiPermissionFlag = false;
            Permission updatePermission = new Permission();
            updatePermission.setId(dbPermission.getPermissionId());
            ApiPermission updateApiPermission = new ApiPermission();
            updateApiPermission.setId(dbPermission.getApiPermissionId());
            boolean strFlagChange = !Objects.equals(permission.getStrFlag(), dbPermission.getStrFlag())
                    && !dbPermission.getStrFlag().startsWith(PermissionStrFlagUtils.AUTO_STR_FLAG)
                    && !permission.getStrFlag().startsWith(PermissionStrFlagUtils.AUTO_STR_FLAG);
            if (strFlagChange) {
                updatePermissionFlag = true;
                updatePermission.setStrFlag(permission.getStrFlag());
            }
            if (!Objects.equals(permission.getApiPath(), dbPermission.getApiPath()) || !Objects.equals(permission.getTitle(), dbPermission.getTitle())) {
                updateApiPermissionFlag = true;
                updateApiPermission.setApiPath(permission.getApiPath());
                updateApiPermission.setTitle(permission.getTitle());
            }
            if (!Objects.equals(permission.getDescription(), dbPermission.getDescription())) {
                updatePermissionFlag = true;
                updatePermission.setDescription(permission.getDescription());
            }
            if (updatePermissionFlag) {
                permissionMapper.updateById(updatePermission);
            }
            if (updateApiPermissionFlag) {
                apiPermissionMapper.updateById(updateApiPermission);
            }
        }
        return notExistPermission;
    }
}
