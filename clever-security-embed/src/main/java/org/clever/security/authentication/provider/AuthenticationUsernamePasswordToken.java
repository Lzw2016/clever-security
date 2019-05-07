package org.clever.security.authentication.provider;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.security.authentication.AuthenticationLoginToken;
import org.clever.security.exception.BadLoginTypeException;
import org.clever.security.service.RequestCryptoService;
import org.clever.security.token.login.BaseLoginToken;
import org.clever.security.token.login.UsernamePasswordToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * UsernamePasswordToken 验证逻辑
 * 作者： lzw<br/>
 * 创建时间：2019-04-28 15:21 <br/>
 */
@Component
@Slf4j
public class AuthenticationUsernamePasswordToken implements AuthenticationLoginToken {

    private UsernamePasswordToken getUsernamePasswordToken(BaseLoginToken loginToken) {
        if (!(loginToken instanceof UsernamePasswordToken)) {
            throw new BadLoginTypeException(String.format("loginToken类型错误,%s | %s", loginToken.getClass(), loginToken.toString()));
        }
        return (UsernamePasswordToken) loginToken;
    }

    @Override
    public boolean supports(Class<?> loginToken) {
        return UsernamePasswordToken.class.isAssignableFrom(loginToken);
    }

    @Override
    public void preAuthenticationCheck(BaseLoginToken loginToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = getUsernamePasswordToken(loginToken);
        if (StringUtils.isBlank(usernamePasswordToken.getUsername())) {
            throw new BadCredentialsException("用户名不能为空");
        }
        if (StringUtils.isBlank(usernamePasswordToken.getPassword())) {
            throw new BadCredentialsException("密码不能为空");
        }
    }

    @Override
    public void mainAuthenticationChecks(BaseLoginToken loginToken, UserDetails loadedUser, RequestCryptoService requestCryptoService, BCryptPasswordEncoder bCryptPasswordEncoder) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = getUsernamePasswordToken(loginToken);
        // 用户密码需要AES对称加解密 网络密文传输
        usernamePasswordToken.setPassword(requestCryptoService.reqAesDecrypt(usernamePasswordToken.getPassword()));
        // 用户名、密码校验
        if (!usernamePasswordToken.getUsername().equals(loadedUser.getUsername()) || !bCryptPasswordEncoder.matches(usernamePasswordToken.getPassword(), loadedUser.getPassword())) {
            log.info("### 用户名密码验证失败 [{}]", loginToken.toString());
            throw new BadCredentialsException("用户名密码验证失败");
        }
        log.info("### 用户名密码验证成功 [{}]", loginToken.toString());
    }
}
