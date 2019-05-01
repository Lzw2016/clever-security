package org.clever.security.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.security.entity.*;
import org.clever.security.mapper.ServiceSysMapper;
import org.clever.security.mapper.UserLoginLogMapper;
import org.clever.security.mapper.UserMapper;
import org.clever.security.model.LoginUserDetails;
import org.clever.security.model.UserAuthority;
import org.clever.security.service.ISecurityContextService;
import org.clever.security.token.SecurityContextToken;
import org.clever.security.token.login.BaseLoginToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-22 19:51 <br/>
 */
@SuppressWarnings("Duplicates")
@Transactional(readOnly = true)
@Service("JwtTokenSecurityContextService")
@Slf4j
public class JwtTokenSecurityContextService implements ISecurityContextService {

    /**
     * SecurityContext Key
     */
    private static final String SecurityContextKey = "security-context";
    /**
     * JWT Token 令牌Key
     */
    private static final String JwtTokenKey = "jwt-token";
    /**
     * JWT Token 刷新令牌Key
     */
    private static final String JwtTokenRefreshKey = "refresh-token";

    @Autowired
    private ServiceSysMapper serviceSysMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserLoginLogMapper userLoginLogMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 生成 Redis 存储 SecurityContextKey
     */
    private String getSecurityContextKey(String redisNamespace, String username) {
        // {redisNamespace}:{SecurityContextKey}:{username}
        return String.format("%s:%s:%s", redisNamespace, SecurityContextKey, username);
    }

    /**
     * 生成 Redis 存储 JwtTokenKey Pattern
     */
    private String getJwtTokenPatternKey(String redisNamespace, String username) {
        // {redisNamespace}:{JwtTokenKey}:{username}:{*}
        return String.format("%s:%s:%s:%s", redisNamespace, JwtTokenKey, username, "*");
    }

    /**
     * 生成 Redis 存储 JwtTokenRefreshKey
     */
    private String getJwtRefreshTokenKey(String redisNamespace, String refreshToken) {
        // {redisNamespace}:{JwtTokenRefreshKey}:{refreshToken}
        return String.format("%s:%s:%s", redisNamespace, JwtTokenRefreshKey, refreshToken);
    }

    /**
     * 加载用户安全信息
     *
     * @param loginToken 当前的loginToken
     * @param sysName    系统名
     * @param userName   用户名
     * @return 不存在返回null
     */
    private SecurityContextToken loadUserLoginToken(BaseLoginToken loginToken, String sysName, String userName) {
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
        // 组装 UserLoginToken
        return new SecurityContextToken(loginToken, userDetails);
    }

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
                || !Objects.equals(EnumConstant.ServiceSys_LoginModel_1, serviceSys.getLoginModel())) {
            return null;
        }
        String securityContextKey = getSecurityContextKey(serviceSys.getRedisNameSpace(), userName);
        Object securityObject = redisTemplate.opsForValue().get(securityContextKey);
        if (securityObject == null) {
            return null;
        }
        Map<String, SecurityContext> map = new HashMap<>();
        SecurityContext securityContext = null;
        if (securityObject instanceof SecurityContext) {
            securityContext = (SecurityContext) securityObject;
        }
        map.put(sysName, securityContext);
        return map;
    }

    @Override
    public SecurityContext getSecurityContext(String tokenId) {
        UserLoginLog userLoginLog = userLoginLogMapper.getBySessionId(tokenId);
        ServiceSys serviceSys = serviceSysMapper.getBySysName(userLoginLog.getSysName());
        if (serviceSys == null
                || StringUtils.isBlank(serviceSys.getRedisNameSpace())
                || !Objects.equals(EnumConstant.ServiceSys_LoginModel_1, serviceSys.getLoginModel())) {
            return null;
        }
        String securityContextKey = getSecurityContextKey(serviceSys.getRedisNameSpace(), userLoginLog.getUsername());
        Object securityObject = redisTemplate.opsForValue().get(securityContextKey);
        if (securityObject == null) {
            return null;
        }
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
                || !Objects.equals(EnumConstant.ServiceSys_LoginModel_1, serviceSys.getLoginModel())) {
            return null;
        }
        String securityContextKey = getSecurityContextKey(serviceSys.getRedisNameSpace(), userName);
        Object oldSecurityObject = redisTemplate.opsForValue().get(securityContextKey);
        if (!(oldSecurityObject instanceof SecurityContext)) {
            return null;
        }
        SecurityContext oldSecurityContext = (SecurityContext) oldSecurityObject;
        if (!(oldSecurityContext.getAuthentication() instanceof SecurityContextToken)) {
            return null;
        }
        SecurityContextToken oldUserLoginToken = (SecurityContextToken) oldSecurityContext.getAuthentication();
        SecurityContextToken newUserLoginToken = loadUserLoginToken(oldUserLoginToken.getLoginToken(), sysName, userName);
        if (newUserLoginToken == null) {
            return null;
        }
        SecurityContext newSecurityContext = loadSecurityContext(newUserLoginToken, oldUserLoginToken);
        newSecurityContextList.add(newSecurityContext);
        redisTemplate.opsForValue().set(securityContextKey, newSecurityContext);
        // 删除 JwtToken
        String JwtTokenKeyPattern = getJwtTokenPatternKey(serviceSys.getRedisNameSpace(), userName);
        Set<String> JwtTokenKeySet = redisTemplate.keys(JwtTokenKeyPattern);
        if (JwtTokenKeySet != null && JwtTokenKeySet.size() > 0) {
            redisTemplate.delete(JwtTokenKeySet);
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
                    || !Objects.equals(EnumConstant.ServiceSys_LoginModel_1, serviceSys.getLoginModel())) {
                continue;
            }
            List<SecurityContext> securityContextList = reloadSecurityContext(serviceSys.getSysName(), userName);
            result.put(serviceSys.getSysName(), securityContextList);
        }
        return result;
    }

    @Override
    public int forcedOffline(String sysName, String userName) {
        int count = 0;
        ServiceSys serviceSys = serviceSysMapper.getBySysName(sysName);
        if (serviceSys == null
                || StringUtils.isBlank(serviceSys.getRedisNameSpace())
                || !Objects.equals(EnumConstant.ServiceSys_LoginModel_1, serviceSys.getLoginModel())) {
            return 0;
        }
        // 删除 JwtToken
        String JwtTokenKeyPattern = getJwtTokenPatternKey(serviceSys.getRedisNameSpace(), userName);
        Set<String> JwtTokenKeySet = redisTemplate.keys(JwtTokenKeyPattern);
        if (JwtTokenKeySet != null && JwtTokenKeySet.size() > 0) {
            redisTemplate.delete(JwtTokenKeySet);
            count = JwtTokenKeySet.size();
        }
        // 删除 RefreshToken
        String jwtRefreshTokenKeyPattern = getJwtRefreshTokenKey(serviceSys.getRedisNameSpace(), userName + ":*");
        Set<String> jwtRefreshTokenKeySet = redisTemplate.keys(jwtRefreshTokenKeyPattern);
        if (jwtRefreshTokenKeySet != null && jwtRefreshTokenKeySet.size() > 0) {
            redisTemplate.delete(jwtRefreshTokenKeySet);
            count = jwtRefreshTokenKeySet.size();
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
                    || !Objects.equals(EnumConstant.ServiceSys_LoginModel_1, serviceSys.getLoginModel())) {
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
                || !Objects.equals(EnumConstant.ServiceSys_LoginModel_1, serviceSys.getLoginModel())) {
            return;
        }
        // 删除 JwtToken
        String JwtTokenKeyPattern = getJwtTokenPatternKey(serviceSys.getRedisNameSpace(), userName);
        Set<String> JwtTokenKeySet = redisTemplate.keys(JwtTokenKeyPattern);
        if (JwtTokenKeySet != null && JwtTokenKeySet.size() > 0) {
            redisTemplate.delete(JwtTokenKeySet);
        }
        // 删除 RefreshToken
        String jwtRefreshTokenKeyPattern = getJwtRefreshTokenKey(serviceSys.getRedisNameSpace(), userName + ":*");
        Set<String> jwtRefreshTokenKeySet = redisTemplate.keys(jwtRefreshTokenKeyPattern);
        if (jwtRefreshTokenKeySet != null && jwtRefreshTokenKeySet.size() > 0) {
            redisTemplate.delete(jwtRefreshTokenKeySet);
        }
    }
}
