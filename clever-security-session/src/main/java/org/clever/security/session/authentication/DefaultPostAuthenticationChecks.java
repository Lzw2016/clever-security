package org.clever.security.session.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Component;

/**
 * 验证帐号信息成功之后的校验
 *
 * 作者： lzw<br/>
 * 创建时间：2018-09-19 16:33 <br/>
 */
@Component("DefaultPostAuthenticationChecks")
@Slf4j
public class DefaultPostAuthenticationChecks implements UserDetailsChecker {

    @Override
    public void check(UserDetails user) {
        if (!user.isCredentialsNonExpired()) {
            log.info("帐号密码已过期 [username={}]", user.getUsername());
            throw new CredentialsExpiredException("帐号密码已过期");
        }
    }
}
