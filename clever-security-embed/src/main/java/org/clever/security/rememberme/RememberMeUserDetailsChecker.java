package org.clever.security.rememberme;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.Constant;
import org.clever.security.exception.CanNotLoginSysException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.stereotype.Component;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-22 15:50 <br/>
 */
@ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "loginModel", havingValue = "session")
@Component
@Slf4j
public class RememberMeUserDetailsChecker implements UserDetailsChecker {

    @Autowired
    @Qualifier("DefaultPreAuthenticationChecks")
    private UserDetailsChecker preAuthenticationChecks;
    @Autowired
    @Qualifier("DefaultPostAuthenticationChecks")
    private UserDetailsChecker postAuthenticationChecks;

    /**
     * {@link org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices#autoLogin}只处理一下异常
     * 1.CookieTheftException<br />
     * 2.UsernameNotFoundException<br />
     * 3.InvalidCookieException<br />
     * 4.AccountStatusException<br />
     * 5.RememberMeAuthenticationException<br />
     * 由于使用自定义UserDetailsService会抛出其他异常，所以包装处理
     */
    @Override
    public void check(UserDetails toCheck) {
        try {
            preAuthenticationChecks.check(toCheck);
            postAuthenticationChecks.check(toCheck);
        } catch (CanNotLoginSysException e) {
            throw new RememberMeAuthenticationException(e.getMessage(), e);
        }
    }
}
