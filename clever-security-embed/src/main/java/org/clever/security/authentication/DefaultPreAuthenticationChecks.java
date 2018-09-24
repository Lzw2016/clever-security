package org.clever.security.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-19 16:32 <br/>
 */
@Slf4j
public class DefaultPreAuthenticationChecks implements UserDetailsChecker {

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
    }
}
