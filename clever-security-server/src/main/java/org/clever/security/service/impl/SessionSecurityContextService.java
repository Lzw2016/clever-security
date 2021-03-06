package org.clever.security.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.security.entity.*;
import org.clever.security.mapper.RememberMeTokenMapper;
import org.clever.security.mapper.ServiceSysMapper;
import org.clever.security.mapper.UserLoginLogMapper;
import org.clever.security.mapper.UserMapper;
import org.clever.security.model.LoginUserDetails;
import org.clever.security.model.UserAuthority;
import org.clever.security.service.ISecurityContextService;
import org.clever.security.token.SecurityContextToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-22 19:33 <br/>
 */
@SuppressWarnings("Duplicates")
@Transactional(readOnly = true)
@Service("sessionSecurityContextService")
@Slf4j
public class SessionSecurityContextService implements ISecurityContextService {

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

    /**
     * 参考 RedisOperationsSessionRepository#getPrincipalKey
     */
    private String getPrincipalKey(String redisNameSpace, String userName) {
        // {redisNameSpace}:{index}:{PRINCIPAL_NAME_INDEX_NAME}:{userName}
        // spring:session:clever-security:index:org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME:lizw
        return redisNameSpace + ":index:" + FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME + ":" + userName;
    }

    /**
     * 获取SessionKey
     */
    private String getSessionKey(String redisNameSpace, String sessionId) {
        // {redisNameSpace}:{sessions}:{sessionId}
        // spring:session:clever-security:sessions:962e63c3-4e2e-4089-94f1-02a9137661b4
        return redisNameSpace + ":sessions:" + sessionId;
    }

    private LoginUserDetails loadUserLoginToken(String sysName, String userName) {
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
        // TODO 加载角色
        // userDetails.getRoles().add();
        return userDetails;
    }

//    /**
//     * 加载用户安全信息
//     *
//     * @param sysName  系统名
//     * @param userName 用户名
//     * @return 不存在返回null
//     */
//    private SecurityContextToken loadUserLoginToken(BaseLoginToken loginToken, String sysName, String userName) {
//        // 获取用所有权限
//        User user = userMapper.getByUnique(userName);
//        if (user == null) {
//            return null;
//        }
//        List<Permission> permissionList = userMapper.findByUsername(userName, sysName);
//        LoginUserDetails userDetails = new LoginUserDetails(user);
//        for (Permission permission : permissionList) {
//            userDetails.getAuthorities().add(new UserAuthority(permission.getPermissionStr(), permission.getTitle()));
//        }
//        // TODO 加载角色
//        // userDetails.getRoles().add();
//        // 组装 UserLoginToken
//        return new SecurityContextToken(loginToken, userDetails);
//    }

    /**
     * 加载用户安全信息
     *
     * @param newUserLoginToken 新的Token
     * @param oldUserLoginToken 旧的Token
     */
    private SecurityContext loadSecurityContext(SecurityContextToken newUserLoginToken, SecurityContextToken oldUserLoginToken) {
        newUserLoginToken.setDetails(oldUserLoginToken.getDetails());
        newUserLoginToken.eraseCredentials();
        // 组装 SecurityContext
        return new SecurityContextImpl(newUserLoginToken);
    }

    @Override
    public Map<String, SecurityContext> getSecurityContext(String sysName, String userName) {
        ServiceSys serviceSys = serviceSysMapper.getBySysName(sysName);
        if (serviceSys == null
                || StringUtils.isBlank(serviceSys.getRedisNameSpace())
                || !Objects.equals(EnumConstant.ServiceSys_LoginModel_0, serviceSys.getLoginModel())) {
            return null;
        }
        String principalKey = getPrincipalKey(serviceSys.getRedisNameSpace(), userName);
        Set<Object> sessionIds = sessionRepository.getSessionRedisOperations().boundSetOps(principalKey).members();
        if (sessionIds == null) {
            return null;
        }
        Map<String, SecurityContext> map = new HashMap<>();
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

    @Override
    public SecurityContext getSecurityContext(String sessionId) {
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

    @Override
    public List<SecurityContext> reloadSecurityContext(String sysName, String userName) {
        List<SecurityContext> newSecurityContextList = new ArrayList<>();
        ServiceSys serviceSys = serviceSysMapper.getBySysName(sysName);
        if (serviceSys == null
                || StringUtils.isBlank(serviceSys.getRedisNameSpace())
                || !Objects.equals(EnumConstant.ServiceSys_LoginModel_0, serviceSys.getLoginModel())) {
            return null;
        }
        String principalKey = getPrincipalKey(serviceSys.getRedisNameSpace(), userName);
        Set<Object> sessionIds = sessionRepository.getSessionRedisOperations().boundSetOps(principalKey).members();
        if (sessionIds == null) {
            return null;
        }
        LoginUserDetails loginUserDetails = null;
        for (Object sessionId : sessionIds) {
            if (sessionId == null || StringUtils.isBlank(sessionId.toString())) {
                continue;
            }
            String sessionKey = getSessionKey(serviceSys.getRedisNameSpace(), sessionId.toString());
            Object securityObject = sessionRepository.getSessionRedisOperations().boundHashOps(sessionKey).get(SPRING_SECURITY_CONTEXT);
            if (!(securityObject instanceof SecurityContext)) {
                log.error("重新加载SecurityContext失败, SecurityContext类型错误，{}", securityObject == null ? "null" : securityObject.getClass());
                continue;
            }
            SecurityContext oldSecurityContext = (SecurityContext) securityObject;
            if (!(oldSecurityContext.getAuthentication() instanceof SecurityContextToken)) {
                log.error("重新加载SecurityContext失败, Authentication类型错误，{}", oldSecurityContext.getAuthentication().getClass());
                continue;
            }
            SecurityContextToken oldUserLoginToken = (SecurityContextToken) oldSecurityContext.getAuthentication();
            if (loginUserDetails == null) {
                loginUserDetails = loadUserLoginToken(sysName, userName);
                if (loginUserDetails == null) {
                    return null;
                }
            }
            SecurityContextToken newUserLoginToken = new SecurityContextToken(oldUserLoginToken.getLoginToken(), loginUserDetails);
            SecurityContext newSecurityContext = loadSecurityContext(newUserLoginToken, oldUserLoginToken);
            sessionRepository.getSessionRedisOperations().boundHashOps(sessionKey).put(SPRING_SECURITY_CONTEXT, newSecurityContext);
            newSecurityContextList.add(newSecurityContext);
        }
        return newSecurityContextList;
    }

    @Override
    public Map<String, List<SecurityContext>> reloadSecurityContext(String userName) {
        Map<String, List<SecurityContext>> result = new HashMap<>();
        List<ServiceSys> sysList = userMapper.findSysByUsername(userName);
        if (sysList == null || sysList.size() <= 0) {
            return result;
        }
        for (ServiceSys serviceSys : sysList) {
            if (serviceSys == null
                    || StringUtils.isBlank(serviceSys.getRedisNameSpace())
                    || !Objects.equals(EnumConstant.ServiceSys_LoginModel_0, serviceSys.getLoginModel())) {
                return null;
            }
            List<SecurityContext> securityContextList = reloadSecurityContext(serviceSys.getSysName(), userName);
            result.put(serviceSys.getSysName(), securityContextList);
        }
        return result;
    }

    @Transactional
    @Override
    public int forcedOffline(String sysName, String userName) {
        int count = 0;
        ServiceSys serviceSys = serviceSysMapper.getBySysName(sysName);
        if (serviceSys == null
                || StringUtils.isBlank(serviceSys.getRedisNameSpace())
                || !Objects.equals(EnumConstant.ServiceSys_LoginModel_0, serviceSys.getLoginModel())) {
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

    @Override
    public void delSession(String userName) {
        List<ServiceSys> sysList = userMapper.findSysByUsername(userName);
        if (sysList == null || sysList.size() <= 0) {
            return;
        }
        for (ServiceSys serviceSys : sysList) {
            if (serviceSys == null
                    || StringUtils.isBlank(serviceSys.getRedisNameSpace())
                    || !Objects.equals(EnumConstant.ServiceSys_LoginModel_0, serviceSys.getLoginModel())) {
                continue;
            }
            delSession(serviceSys.getSysName(), userName);
        }
    }

    @Override
    public void delSession(String sysName, String userName) {
        ServiceSys serviceSys = serviceSysMapper.getBySysName(sysName);
        if (serviceSys == null
                || StringUtils.isBlank(serviceSys.getRedisNameSpace())
                || !Objects.equals(EnumConstant.ServiceSys_LoginModel_0, serviceSys.getLoginModel())) {
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
}
