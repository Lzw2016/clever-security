package org.clever.security.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.security.entity.*;
import org.clever.security.jwt.model.LoginUserDetails;
import org.clever.security.jwt.model.UserAuthority;
import org.clever.security.jwt.model.UserLoginToken;
import org.clever.security.mapper.ServiceSysMapper;
import org.clever.security.mapper.UserLoginLogMapper;
import org.clever.security.mapper.UserMapper;
import org.clever.security.service.ISessionService;
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
@Service("jwtTokenSessionService")
@Slf4j
public class JwtTokenSessionService implements ISessionService {

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

    @Override
    public Map<String, SecurityContext> getSessionSecurityContext(String sysName, String userName) {
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
    public SecurityContext getSessionSecurityContext(String tokenId) {
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
    public SecurityContext reloadSessionSecurityContext(String sysName, String userName) {
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
        if (!(oldSecurityContext.getAuthentication() instanceof UserLoginToken)) {
            return null;
        }
        UserLoginToken oldUserLoginToken = (UserLoginToken) oldSecurityContext.getAuthentication();
        UserLoginToken newUserLoginToken = loadUserLoginToken(sysName, userName);
        if (newUserLoginToken == null) {
            return null;
        }
        SecurityContext newSecurityContext = loadSecurityContext(newUserLoginToken, oldUserLoginToken);
        redisTemplate.opsForValue().set(securityContextKey, newSecurityContext);
        // 删除 JwtToken
        String jwtTokenKeyPattern = getJwtTokenPatternKey(serviceSys.getRedisNameSpace(), userName);
        Set<String> jwtTokenKeySet = redisTemplate.keys(jwtTokenKeyPattern);
        if (jwtTokenKeySet != null && jwtTokenKeySet.size() > 0) {
            redisTemplate.delete(jwtTokenKeySet);
        }
        return newSecurityContext;
    }

    @Override
    public Map<String, SecurityContext> reloadSessionSecurityContext(String userName) {
        Map<String, SecurityContext> result = new HashMap<>();
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
            SecurityContext securityContext = reloadSessionSecurityContext(serviceSys.getSysName(), userName);
            result.put(serviceSys.getSysName(), securityContext);
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
        String jwtTokenKeyPattern = getJwtTokenPatternKey(serviceSys.getRedisNameSpace(), userName);
        Set<String> jwtTokenKeySet = redisTemplate.keys(jwtTokenKeyPattern);
        if (jwtTokenKeySet != null && jwtTokenKeySet.size() > 0) {
            redisTemplate.delete(jwtTokenKeySet);
            count = jwtTokenKeySet.size();
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
        String jwtTokenKeyPattern = getJwtTokenPatternKey(serviceSys.getRedisNameSpace(), userName);
        Set<String> jwtTokenKeySet = redisTemplate.keys(jwtTokenKeyPattern);
        if (jwtTokenKeySet != null && jwtTokenKeySet.size() > 0) {
            redisTemplate.delete(jwtTokenKeySet);
        }
        // 删除 RefreshToken
        String jwtRefreshTokenKeyPattern = getJwtRefreshTokenKey(serviceSys.getRedisNameSpace(), userName + ":*");
        Set<String> jwtRefreshTokenKeySet = redisTemplate.keys(jwtRefreshTokenKeyPattern);
        if (jwtRefreshTokenKeySet != null && jwtRefreshTokenKeySet.size() > 0) {
            redisTemplate.delete(jwtRefreshTokenKeySet);
        }
    }
}
