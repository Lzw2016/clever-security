package org.clever.security.authentication;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.UserClient;
import org.clever.security.config.SecurityConfig;
import org.clever.security.exception.CanNotLoginSysException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Component;

/**
 * 验证帐号信息之前的校验
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-09-19 16:32 <br/>
 */
@SuppressWarnings("Duplicates")
@Component("DefaultPreAuthenticationChecks")
@Slf4j
public class DefaultPreAuthenticationChecks implements UserDetailsChecker {

    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private UserClient userClient;

    @Override
    public void check(UserDetails user) {
        if (!user.isAccountNonLocked()) {
            log.info("帐号已锁定 [username={}]", user.getUsername());
            throw new LockedException("帐号已锁定");
        }
        if (!user.isEnabled()) {
            log.info("帐号已禁用 [username={}]", user.getUsername());
            throw new DisabledException("帐号已禁用");
        }
        if (!user.isAccountNonExpired()) {
            log.info("帐号已过期 [username={}]", user.getUsername());
            throw new AccountExpiredException("帐号已过期");
        }
        // 校验用户是否有权登录当前系统
        Boolean canLogin = userClient.canLogin(user.getUsername(), securityConfig.getSysName());
        if (!canLogin) {
            throw new CanNotLoginSysException("您无权登录当前系统，请联系管理员授权");
        }
    }
}
