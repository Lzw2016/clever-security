package org.clever.security.service.internal;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.mapper.QueryMapper;
import org.clever.security.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-11 20:39 <br/>
 */
@Transactional(readOnly = true)
@Service
@Slf4j
public class UserBindRoleService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QueryMapper queryMapper;

    /**
     * 重新为用户分配角色
     *
     * @param userName     用户登录名
     * @param roleNameList 权限名称集合
     */
    @Transactional
    public void resetUserBindRole(String userName, Collection<String> roleNameList) {
        if (roleNameList == null) {
            roleNameList = new ArrayList<>();
        }
        // 获取关联系统列表
        List<String> oldRoleNameList = queryMapper.findRoleNameByUser(userName);
        Set<String> addRoleName = new HashSet<>(roleNameList);
        addRoleName.removeAll(oldRoleNameList);
        Set<String> delRoleName = new HashSet<>(oldRoleNameList);
        delRoleName.removeAll(roleNameList);
        // 新增
        for (String roleName : addRoleName) {
            userMapper.addRole(userName, roleName);
        }
        // 删除
        for (String roleName : delRoleName) {
            userMapper.delRole(userName, roleName);
        }
        // TODO 跟新Session
    }
}
