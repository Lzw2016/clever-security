package org.clever.security.service;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.server.service.BaseService;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.dto.request.WebPermissionInitReq;
import org.clever.security.dto.request.WebPermissionModelGetReq;
import org.clever.security.dto.response.WebPermissionInitRes;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.Permission;
import org.clever.security.entity.WebPermission;
import org.clever.security.entity.model.WebPermissionModel;
import org.clever.security.mapper.PermissionMapper;
import org.clever.security.mapper.WebPermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-20 15:53 <br/>
 */
@Transactional(readOnly = true)
@Service
@Slf4j
public class WebPermissionService extends BaseService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private WebPermissionMapper webPermissionMapper;

    /**
     * 保存Web权限
     */
    @Transactional
    public void saveWebPermission(WebPermissionModel webPermissionModel) {
        Permission permission = BeanMapper.mapper(webPermissionModel, Permission.class);
        WebPermission webPermission = BeanMapper.mapper(webPermissionModel, WebPermission.class);
        permissionMapper.insert(permission);
        webPermissionMapper.insert(webPermission);
    }

    public WebPermissionModel getWebPermissionModel(WebPermissionModelGetReq req) {
        return webPermissionMapper.getBySysNameAndTarget(
                req.getSysName(),
                req.getTargetClass(),
                req.getTargetMethod(),
                req.getTargetMethodParams()
        );
    }

    public List<WebPermissionModel> findAllWebPermissionModel(String sysName) {
        return webPermissionMapper.findBySysName(sysName);
    }

    /**
     * 初始化某个系统的所有Web权限
     */
    @Transactional
    public WebPermissionInitRes initWebPermission(String sysName, WebPermissionInitReq req) {
        List<WebPermissionModel> allPermission = req.getAllPermission();
        // 校验数据正确
        for (WebPermissionModel webPermissionModel : allPermission) {
            webPermissionModel.check();
        }
        // 排序
        Collections.sort(allPermission);
        // 加载当前模块所有的权限信息 - moduleAllPermission
        List<WebPermissionModel> moduleAllPermission = webPermissionMapper.findBySysName(sysName);
        // 保存系统中有而数据库里没有的资源 - addPermission
        List<WebPermissionModel> addPermission = this.addPermission(allPermission, moduleAllPermission);
        // 统计数据库中有而系统中没有的资源数据 - notExistPermission
        moduleAllPermission = webPermissionMapper.findBySysName(sysName);
        List<WebPermissionModel> notExistPermission = this.getNotExistPermission(allPermission, moduleAllPermission);
        // 构造返回信息
        WebPermissionInitRes res = new WebPermissionInitRes();
        res.setAddPermission(addPermission);
        res.setNotExistPermission(notExistPermission);
        return res;
    }

    /**
     * 保存所有新增的权限 (module targetClass targetMethod resourcesUrl)
     *
     * @param allPermission       当前模块所有权限
     * @param moduleAllPermission 数据库中当前模块所有的权限信息
     * @return 新增的权限
     */
    @Transactional
    protected List<WebPermissionModel> addPermission(List<WebPermissionModel> allPermission, List<WebPermissionModel> moduleAllPermission) {
        List<WebPermissionModel> addPermission = new ArrayList<>();
        Set<String> setPermission = new HashSet<>();
        // module targetClass targetMethod targetMethodParams
        String format = "%1$-128s|%2$-255s|%3$-255s|%4$-255s";
        for (WebPermissionModel permission : moduleAllPermission) {
            setPermission.add(String.format(format,
                    permission.getSysName(),
                    permission.getTargetClass(),
                    permission.getTargetMethod(),
                    permission.getTargetMethodParams()));
        }
        for (WebPermissionModel permission : allPermission) {
            String key = String.format(format,
                    permission.getSysName(),
                    permission.getTargetClass(),
                    permission.getTargetMethod(),
                    permission.getTargetMethodParams());
            if (!setPermission.contains(key)) {
                // 新增不存在的权限配置
                this.saveWebPermission(permission);
                addPermission.add(permission);
            }
        }
        return addPermission;
    }

    /**
     * 统计数据库中有而系统中没有的资源数据
     *
     * @param allPermission       当前模块所有权限
     * @param moduleAllPermission 数据库中当前模块所有的权限信息
     * @return 数据库中有而系统中没有的资源数据
     */
    @Transactional
    protected List<WebPermissionModel> getNotExistPermission(List<WebPermissionModel> allPermission, List<WebPermissionModel> moduleAllPermission) {
        List<WebPermissionModel> notExistPermission = new ArrayList<>();
        Map<String, WebPermissionModel> mapPermission = new HashMap<>();
        // module targetClass targetMethod targetMethodParams
        String format = "%1$-128s|%2$-255s|%3$-255s|%4$-255s";
        for (WebPermissionModel permission : allPermission) {
            mapPermission.put(String.format(format,
                    permission.getSysName(),
                    permission.getTargetClass(),
                    permission.getTargetMethod(),
                    permission.getTargetMethodParams()), permission);
        }
        for (WebPermissionModel dbPermission : moduleAllPermission) {
            String key = String.format(format,
                    dbPermission.getSysName(),
                    dbPermission.getTargetClass(),
                    dbPermission.getTargetMethod(),
                    dbPermission.getTargetMethodParams());
            WebPermissionModel permission = mapPermission.get(key);
            if (permission == null) {
                // 数据库里有 系统中没有 - 更新 targetExist
                if (!Objects.equals(dbPermission.getTargetExist(), EnumConstant.WebPermission_targetExist_0)) {
                    WebPermission update = new WebPermission();
                    update.setId(dbPermission.getWebPermissionId());
                    update.setTargetExist(EnumConstant.WebPermission_targetExist_0);
                    update.setUpdateAt(new Date());
                    webPermissionMapper.updateById(update);
                }
                notExistPermission.add(dbPermission);
            } else {
                // 数据库里有 系统中也有 - 更新 resourcesUrl
                boolean permissionStrChange = !Objects.equals(permission.getPermissionStr(), dbPermission.getPermissionStr())
                        && !(dbPermission.getPermissionStr().startsWith("[auto]:") && permission.getPermissionStr().startsWith("[auto]:"));
                if (!Objects.equals(permission.getResourcesUrl(), dbPermission.getResourcesUrl())
                        || permissionStrChange) {
                    WebPermission update = new WebPermission();
                    update.setId(dbPermission.getPermissionId());
                    update.setResourcesUrl(permission.getResourcesUrl());
                    update.setPermissionStr(permission.getPermissionStr());
                    webPermissionMapper.updateById(update);
                }
                if (!Objects.equals(permission.getTitle(), dbPermission.getTitle())
                        || !Objects.equals(permission.getDescription(), dbPermission.getDescription())
                        || permissionStrChange) {
                    Permission update = new Permission();
                    update.setId(dbPermission.getPermissionId());
                    update.setTitle(permission.getTitle());
                    update.setDescription(permission.getDescription());
                    update.setPermissionStr(permission.getPermissionStr());
                    permissionMapper.updateById(update);
                }
            }
        }
        return notExistPermission;
    }
}
