package org.clever.security.service.internal;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.mapper.RoleMapper;
import org.clever.security.service.ISecurityContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 重新加载Session
 * 作者： lzw<br/>
 * 创建时间：2018-11-16 11:54 <br/>
 */
@Transactional(readOnly = true)
@Service
@Slf4j
public class ReLoadSessionService {

    // @Autowired
    // private UserMapper userMapper;
    // @Autowired
    // private RedisOperationsSessionRepository sessionRepository;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private ISecurityContextService sessionService;

    /**
     * 角色所拥有的权限发生变化 - 重新加载Session
     */
    public void onChangeRole(String roleName) {
        List<String> usernameList = roleMapper.findUsernameByRoleName(roleName);
        for (String username : usernameList) {
            sessionService.reloadSecurityContext(username);
        }
    }

    /**
     * 角色所拥有的权限发生变化 - 重新加载Session
     */
    public void onChangeRole(Collection<String> roleNameList) {
        Set<String> usernameList = new HashSet<>();
        for (String roleName : roleNameList) {
            usernameList.addAll(roleMapper.findUsernameByRoleName(roleName));
        }
        for (String username : usernameList) {
            sessionService.reloadSecurityContext(username);
        }
    }
}
