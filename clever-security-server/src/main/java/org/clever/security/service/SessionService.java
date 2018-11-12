package org.clever.security.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.security.entity.Permission;
import org.clever.security.entity.ServiceSys;
import org.clever.security.entity.User;
import org.clever.security.entity.UserLoginLog;
import org.clever.security.mapper.RememberMeTokenMapper;
import org.clever.security.mapper.ServiceSysMapper;
import org.clever.security.mapper.UserLoginLogMapper;
import org.clever.security.mapper.UserMapper;
import org.clever.security.model.LoginUserDetails;
import org.clever.security.model.UserAuthority;
import org.clever.security.model.UserLoginToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-12 10:16 <br/>
 */
@Transactional(readOnly = true)
@Service
@Slf4j
public class SessionService {

    private static final String SPRING_SECURITY_CONTEXT = "sessionAttr:SPRING_SECURITY_CONTEXT";

    @Autowired
    private ServiceSysMapper serviceSysMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserLoginLogMapper userLoginLogMapper;
    @Autowired
    private RememberMeTokenMapper rememberMeTokenMapper;
    @Autowired
    private RedisOperationsSessionRepository sessionRepository;
//    @Autowired
//    private SpringSessionBackedSessionRegistry sessionRegistry;

    /**
     * 参考 RedisOperationsSessionRepository#getPrincipalKey
     */
    private String getPrincipalKey(String redisNameSpace, String userName) {
        // {redisNameSpace}:{index}:{PRINCIPAL_NAME_INDEX_NAME}:{userName}
        // spring:session:clever-security:index:org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME:lizw
        return redisNameSpace + ":index:" + FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME + ":" + userName;
    }

    /**
     * 删除用户Session(所有的系统)
     *
     * @param userName 用户信息
     */
    public void delSession(String userName) {
        List<String> sysNames = userMapper.findSysNameByUsername(userName);
        if (sysNames == null || sysNames.size() <= 0) {
            return;
        }
        for (String sysName : sysNames) {
            delSession(sysName, userName);
        }
    }

    /**
     * 删除用户Session
     *
     * @param sysName  系统名
     * @param userName 用户名
     */
    public void delSession(String sysName, String userName) {
        ServiceSys serviceSys = serviceSysMapper.getBySysName(sysName);
        if (serviceSys == null || StringUtils.isBlank(serviceSys.getRedisNameSpace())) {
            return;
        }
        String principalKey = getPrincipalKey(serviceSys.getRedisNameSpace(), userName);
        Set<Object> sessionIds = sessionRepository.getSessionRedisOperations().boundSetOps(principalKey).members();
        if (sessionIds == null) {
            return;
        }
        for (Object sessionId : sessionIds) {
            if (sessionId == null || StringUtils.isBlank(sessionId.toString())) {
                continue;
            }
            sessionRepository.deleteById(sessionId.toString());
        }
    }

    /**
     * 获取SessionKey
     */
    private String getSessionKey(String redisNameSpace, String sessionId) {
        // {redisNameSpace}:{sessions}:{sessionId}
        // spring:session:clever-security:sessions:962e63c3-4e2e-4089-94f1-02a9137661b4
        return redisNameSpace + ":sessions:" + sessionId;
    }

    /**
     * 加载用户安全信息
     *
     * @param sysName  系统名
     * @param userName 用户名
     * @return 不存在返回null
     */
    private UserLoginToken loadUserLoginToken(String sysName, String userName) {
        // 获取用所有权限
        User user = userMapper.getByUnique(userName);
        if (user == null) {
            return null;
        }
        List<Permission> permissionList = userMapper.findByUsername(userName, sysName);
        LoginUserDetails userDetails = new LoginUserDetails(user);
        for (Permission permission : permissionList) {
            userDetails.getAuthorities().add(new UserAuthority(permission.getPermissionStr(), permission.getTitle()));
        }
        // 组装 UserLoginToken
        return new UserLoginToken(userDetails);
    }

    /**
     * 加载用户安全信息
     *
     * @param newUserLoginToken 新的Token
     * @param oldUserLoginToken 旧的Token
     */
    private SecurityContext loadSecurityContext(UserLoginToken newUserLoginToken, UserLoginToken oldUserLoginToken) {
        newUserLoginToken.setLoginType(oldUserLoginToken.getLoginType());
        newUserLoginToken.setUsername(oldUserLoginToken.getUsername());
        newUserLoginToken.setPassword(oldUserLoginToken.getPassword());
        newUserLoginToken.setDetails(oldUserLoginToken.getDetails());
        newUserLoginToken.eraseCredentials();
        // 组装 SecurityContext
        return new SecurityContextImpl(newUserLoginToken);
    }


    /**
     * 重新加载所有系统用户权限信息(sessionAttr:SPRING_SECURITY_CONTEXT)
     *
     * @param sysName  系统名
     * @param userName 用户名
     */
    public SecurityContext reloadSessionSecurityContext(String sysName, String userName) {
        ServiceSys serviceSys = serviceSysMapper.getBySysName(sysName);
        if (serviceSys == null || StringUtils.isBlank(serviceSys.getRedisNameSpace())) {
            return null;
        }
        String principalKey = getPrincipalKey(serviceSys.getRedisNameSpace(), userName);
        Set<Object> sessionIds = sessionRepository.getSessionRedisOperations().boundSetOps(principalKey).members();
        if (sessionIds == null) {
            return null;
        }
        UserLoginToken newUserLoginToken = null;
        SecurityContext newSecurityContext = null;
        for (Object sessionId : sessionIds) {
            if (sessionId == null || StringUtils.isBlank(sessionId.toString())) {
                continue;
            }
            String sessionKey = getSessionKey(serviceSys.getRedisNameSpace(), sessionId.toString());
            Object securityObject = sessionRepository.getSessionRedisOperations().boundHashOps(sessionKey).get(SPRING_SECURITY_CONTEXT);
            if (newUserLoginToken == null) {
                newUserLoginToken = loadUserLoginToken(sysName, userName);
                if (newUserLoginToken == null) {
                    return null;
                }
            }
            if (securityObject instanceof SecurityContext) {
                SecurityContext oldSecurityContext = (SecurityContext) securityObject;
                if (oldSecurityContext.getAuthentication() instanceof UserLoginToken) {
                    UserLoginToken oldUserLoginToken = (UserLoginToken) oldSecurityContext.getAuthentication();
                    newSecurityContext = loadSecurityContext(newUserLoginToken, oldUserLoginToken);
                    sessionRepository.getSessionRedisOperations().boundHashOps(sessionKey).put(SPRING_SECURITY_CONTEXT, newSecurityContext);
                }
            }
        }
        return newSecurityContext;
    }

    /**
     * 重新加载所有系统用户权限信息(sessionAttr:SPRING_SECURITY_CONTEXT)
     *
     * @param userName 用户名
     */
    public Map<String, SecurityContext> reloadSessionSecurityContext(String userName) {
        Map<String, SecurityContext> result = new HashMap<>();
        List<String> sysNames = userMapper.findSysNameByUsername(userName);
        if (sysNames == null || sysNames.size() <= 0) {
            return result;
        }
        for (String sysName : sysNames) {
            SecurityContext securityContext = reloadSessionSecurityContext(sysName, userName);
            result.put(sysName, securityContext);
        }
        return result;
    }

    /**
     * 读取用户SessionSecurityContext
     *
     * @param sysName  系统名
     * @param userName 用户名
     * @return 不存在返回null
     */
    public Map<String, SecurityContext> getSessionSecurityContext(String sysName, String userName) {
        Map<String, SecurityContext> map = new HashMap<>();
        ServiceSys serviceSys = serviceSysMapper.getBySysName(sysName);
        if (serviceSys == null || StringUtils.isBlank(serviceSys.getRedisNameSpace())) {
            return null;
        }
        String principalKey = getPrincipalKey(serviceSys.getRedisNameSpace(), userName);
        Set<Object> sessionIds = sessionRepository.getSessionRedisOperations().boundSetOps(principalKey).members();
        if (sessionIds == null) {
            return null;
        }
        for (Object sessionId : sessionIds) {
            if (sessionId == null || StringUtils.isBlank(sessionId.toString())) {
                continue;
            }
            String sessionKey = getSessionKey(serviceSys.getRedisNameSpace(), sessionId.toString());
            Object securityObject = sessionRepository.getSessionRedisOperations().boundHashOps(sessionKey).get(SPRING_SECURITY_CONTEXT);
            SecurityContext securityContext = null;
            if (securityObject instanceof SecurityContext) {
                securityContext = (SecurityContext) securityObject;
            }
            map.put(sessionId.toString(), securityContext);
        }
        return map;
    }

    /**
     * 读取用户SessionSecurityContext
     *
     * @param sessionId Session ID
     */
    public SecurityContext getSessionSecurityContext(String sessionId) {
        if (sessionId == null || StringUtils.isBlank(sessionId)) {
            return null;
        }
        UserLoginLog userLoginLog = userLoginLogMapper.getBySessionId(sessionId);
        if (userLoginLog == null || StringUtils.isBlank(userLoginLog.getSysName())) {
            return null;
        }
        ServiceSys serviceSys = serviceSysMapper.getBySysName(userLoginLog.getSysName());
        if (serviceSys == null || StringUtils.isBlank(serviceSys.getRedisNameSpace())) {
            return null;
        }
        String sessionKey = getSessionKey(serviceSys.getRedisNameSpace(), sessionId);
        Object securityObject = sessionRepository.getSessionRedisOperations().boundHashOps(sessionKey).get(SPRING_SECURITY_CONTEXT);
        if (securityObject instanceof SecurityContext) {
            return (SecurityContext) securityObject;
        }
        return null;
    }


    /**
     * 踢出用户(强制下线)
     *
     * @param sysName  系统名
     * @param userName 用户名
     * @return 下线Session数量
     */
    @Transactional
    public int forcedOffline(String sysName, String userName) {
        int count = 0;
        ServiceSys serviceSys = serviceSysMapper.getBySysName(sysName);
        if (serviceSys == null || StringUtils.isBlank(serviceSys.getRedisNameSpace())) {
            return 0;
        }
        String principalKey = getPrincipalKey(serviceSys.getRedisNameSpace(), userName);
        Set<Object> sessionIds = sessionRepository.getSessionRedisOperations().boundSetOps(principalKey).members();
        if (sessionIds == null) {
            return 0;
        }
        // 删除 RememberMe Token
        rememberMeTokenMapper.deleteBySysNameAndUsername(sysName, userName);
        // 删除Session
        for (Object sessionId : sessionIds) {
            if (sessionId == null || StringUtils.isBlank(sessionId.toString())) {
                continue;
            }
            count++;
            sessionRepository.deleteById(sessionId.toString());
        }
        return count;
    }
}
