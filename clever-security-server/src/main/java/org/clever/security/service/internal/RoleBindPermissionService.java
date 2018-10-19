package org.clever.security.service.internal;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.mapper.QueryMapper;
import org.clever.security.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-19 19:30 <br/>
 */
@Transactional(readOnly = true)
@Service
@Slf4j
public class RoleBindPermissionService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private QueryMapper queryMapper;

    /**
     * 重新为角色分配权限
     *
     * @param roleName          角色名称
     * @param permissionStrList 权限名称集合
     */
    public void resetRoleBindPermission(String roleName, Collection<String> permissionStrList) {
        if (permissionStrList == null) {
            permissionStrList = new ArrayList<>();
        }
        // 获取关联角色列表
        List<String> oldPermissionStrList = queryMapper.findPermissionStrByRole(roleName);
        Set<String> addPermissionStr = new HashSet<>(permissionStrList);
        addPermissionStr.removeAll(oldPermissionStrList);
        Set<String> delPermissionStr = new HashSet<>(oldPermissionStrList);
        delPermissionStr.removeAll(permissionStrList);
        // 新增
        for (String permissionStr : addPermissionStr) {
            roleMapper.addPermission(roleName, permissionStr);
        }
        // 删除
        for (String permissionStr : delPermissionStr) {
            roleMapper.delPermission(roleName, permissionStr);
        }
        // TODO 分析受影响的用户并更新对应的Session
    }
}
