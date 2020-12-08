package org.clever.security.embed.authentication.login;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.AesKeyConfig;
import org.clever.security.embed.config.internal.LoginConfig;
import org.clever.security.embed.exception.*;
import org.clever.security.entity.User;
import org.clever.security.model.UserInfo;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.clever.security.model.login.LoginNamePasswordReq;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/01 21:43 <br/>
 */
@Slf4j
public class DefaultVerifyUserInfo implements VerifyUserInfo {
    @Override
    public boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserLoginReq loginReq, UserInfo userInfo) {
        return true;
    }

    @Override
    public void verify(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserLoginReq loginReq, UserInfo userInfo) throws LoginException {
        if (loginReq == null) {
            throw new LoginDataValidateException("登录数据为空");
        }
        LoginConfig loginConfig = securityConfig.getLogin();
        AesKeyConfig loginReqAesKey = securityConfig.getLoginReqAesKey();
        // 登录用户不存在
        // 密码错误
        verifyUserInfo(loginConfig, loginReqAesKey, loginReq, userInfo);
        // 不支持登录域错误
        // 用户过期错误
        // 用户禁用错误
        verifyUserStatus(userInfo);
    }

    /**
     * 验证用户信息
     */
    protected void verifyUserInfo(LoginConfig loginConfig, AesKeyConfig loginReqAesKey, AbstractUserLoginReq loginReq, UserInfo userInfo) {
        // 登录用户不存在
        if (userInfo == null) {
            if (loginConfig.isHideUserNotFoundException()) {
                throw new BadCredentialsException("用户名或密码错误");
            } else {
                throw new LoginNameNotFoundException("登录名不存在");
            }
        }
        // 密码错误
        if (loginReq instanceof LoginNamePasswordReq) {
            LoginNamePasswordReq loginNamePasswordReq = (LoginNamePasswordReq) loginReq;
            String reqPassword = loginNamePasswordReq.getPassword();
            if (loginReqAesKey.isEnable()) {
                // TODO 解密密码
                // reqPassword = xxx
            }
            // TODO 加密密码
            // reqPassword = xxx
            if (!Objects.equals(userInfo.getPassword(), reqPassword)) {
                throw new BadCredentialsException(loginConfig.isHideUserNotFoundException() ? "用户名或密码错误" : "登录密码错误");
            }
        }
    }

    /**
     * 验证用户状态
     */
    protected void verifyUserStatus(UserInfo userInfo) {
        // TODO 不支持登录域错误??
        // TODO 获取用户信息
        User user = null;
        if (user.getExpiredTime() != null && new Date().compareTo(user.getExpiredTime()) >= 0) {
            throw new UserExpiredException("登录账号已过期");
        }
        if (!Objects.equals(user.getEnabled(), 1)) {
            throw new UserDisabledException("登录账号被禁用");
        }
    }

    // TODO 不支持登录域错误??

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
