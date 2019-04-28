package org.clever.security.authentication;

import org.clever.security.service.RequestCryptoService;
import org.clever.security.token.BaseLoginToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 认证登录用户信息接口
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2019-04-26 16:45 <br/>
 */
public interface AuthenticationLoginToken {

    /**
     * 是否支持验证此身份类型(BaseLoginToken.class.isAssignableFrom(loginToken))
     *
     * @param loginToken Token类型
     * @return 返回true表示支持认证
     */
    boolean supports(Class<?> loginToken);

    /**
     * loginToken 数据完整性校验(登录认证之前的校验 BadCredentialsException)
     */
    void preAuthenticationCheck(BaseLoginToken loginToken) throws AuthenticationException;

    /**
     * Token验证(密码验证等)
     *
     * @param loginToken            登录Token
     * @param loadedUser            数据库中的数据
     * @param requestCryptoService  请求参数加密/解密
     * @param bCryptPasswordEncoder 密码匹配
     * @throws AuthenticationException (BadCredentialsException)
     */
    void mainAuthenticationChecks(
            BaseLoginToken loginToken,
            UserDetails loadedUser,
            RequestCryptoService requestCryptoService,
            BCryptPasswordEncoder bCryptPasswordEncoder) throws AuthenticationException;
}
