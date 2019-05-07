package org.clever.security.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.common.server.service.BaseService;
import org.clever.security.dto.request.UserAuthenticationReq;
import org.clever.security.dto.response.UserAuthenticationRes;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.Permission;
import org.clever.security.entity.User;
import org.clever.security.mapper.UserMapper;
import org.clever.security.service.internal.ManageCryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-17 9:20 <br/>
 */
@Transactional(readOnly = true)
@Service
@Slf4j
public class UserService extends BaseService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ManageCryptoService manageCryptoService;

    public User getUser(String unique) {
        return userMapper.getByUnique(unique);
    }

    public List<Permission> findAllPermission(String username, String sysName) {
        return userMapper.findByUsername(username, sysName);
    }

    public Boolean canLogin(String username, String sysName) {
        return userMapper.existsUserBySysName(username, sysName) >= 1;
    }

    // TODO 认证
    public Boolean authentication(UserAuthenticationReq req) {
        User user = userMapper.getByUnique(req.getLoginName());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
//        if (!user.isCredentialsNonExpired()) {
//            log.info("帐号密码已过期 [username={}]", user.getUsername());
//            throw new CredentialsExpiredException("帐号密码已过期");
//        }
        if (!Objects.equals(user.getLocked(), EnumConstant.User_Locked_0)) {
            log.info("帐号已锁定 [username={}]", user.getUsername());
            throw new BusinessException("帐号已锁定");
        }
        if (!Objects.equals(user.getEnabled(), EnumConstant.User_Enabled_1)) {
            log.info("帐号已禁用 [username={}]", user.getUsername());
            throw new BusinessException("帐号已禁用");
        }
        if (!(user.getExpiredTime() == null || user.getExpiredTime().compareTo(new Date()) > 0)) {
            log.info("帐号已过期 [username={}]", user.getUsername());
            throw new BusinessException("帐号已过期");
        }
        // 校验用户是否有权登录当前系统
        if (StringUtils.isNotBlank(req.getSysName())) {
            if (!canLogin(user.getUsername(), req.getSysName())) {
                throw new BusinessException("您无权登录当前系统，请联系管理员授权");
            }
        }
        // 校验密码
        if (UserAuthenticationReq.LoginType_Username.equals(req.getLoginType())) {
            // 密码先解密再加密
            req.setPassword(manageCryptoService.reqAesDecrypt(req.getPassword()));
            req.setPassword(manageCryptoService.dbEncode(req.getPassword()));
            // 用户名、密码校验
            if (!req.getLoginName().equals(user.getUsername()) || !manageCryptoService.dbMatches(req.getPassword(), user.getPassword())) {
                log.info("### 用户名密码验证失败 [{}]", req.toString());
                throw new BusinessException("用户名密码验证失败");
            }
            log.info("### 用户名密码验证成功 [{}]", req.toString());
        } else if (UserAuthenticationReq.LoginType_Telephone.equals(req.getLoginType())) {
            // TODO 手机号验证码登录
            throw new BusinessException("暂不支持手机号验证码登录");
        } else {
            throw new BusinessException("不支持的登录类型");
        }
        return true;
    }

    public UserAuthenticationRes authenticationAndRes(UserAuthenticationReq req) {
        UserAuthenticationRes userAuthenticationRes = new UserAuthenticationRes();
        if (StringUtils.isBlank(req.getLoginType())) {
            req.setLoginType("username");
        }
        try {
            userAuthenticationRes.setSuccess(authentication(req));
        } catch (Exception e) {
            userAuthenticationRes.setSuccess(false);
            userAuthenticationRes.setFailMessage(e.getMessage());
        }
        return userAuthenticationRes;
    }
}
