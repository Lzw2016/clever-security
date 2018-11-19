package org.clever.security.jwt.authentication;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.security.jwt.config.SecurityConfig;
import org.clever.security.jwt.exception.BadLoginTypeException;
import org.clever.security.jwt.exception.ConcurrentLoginException;
import org.clever.security.jwt.model.UserLoginToken;
import org.clever.security.jwt.service.GenerateKeyService;
import org.clever.security.jwt.service.JWTTokenService;
import org.clever.security.jwt.service.LoginPasswordCryptoService;
import org.clever.security.jwt.service.LoginUserDetailsService;
import org.clever.security.jwt.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义认证 UserLoginToken
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-03-15 17:41 <br/>
 */
@SuppressWarnings("Duplicates")
@Component
@Slf4j
public class UserLoginTokenAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private LoginUserDetailsService userDetailsService;
    // 帐号校验
    @Autowired
    @Qualifier("DefaultPreAuthenticationChecks")
    private UserDetailsChecker preAuthenticationChecks;
    @Autowired
    @Qualifier("DefaultPostAuthenticationChecks")
    private UserDetailsChecker postAuthenticationChecks;
    @Autowired
    private LoginPasswordCryptoService loginPasswordCryptoService;
    @Autowired
    private JWTTokenService jwtTokenService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private GenerateKeyService generateKeyService;
    // 使用缓存?
    private UserCache userCache = new NullUserCache();
    // 解析授权信息
    private AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
    /**
     * 同一个用户并发登录次数限制(-1表示不限制)
     */
    private final int concurrentLoginCount;

    /**
     * 同一个用户并发登录次数达到最大值之后，是否不允许之后的登录(false 之后登录的把之前登录的挤下来)
     */
    private final boolean notAllowAfterLogin;

    public UserLoginTokenAuthenticationProvider(SecurityConfig securityConfig) {
        SecurityConfig.Login login = securityConfig.getLogin();
        if (login == null) {
            throw new BusinessException("未配置Login配置");
        }
        concurrentLoginCount = login.getConcurrentLoginCount();
        notAllowAfterLogin = login.getNotAllowAfterLogin();
    }

    /**
     * 认证用户登录
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication currentLogined = SecurityContextHolder.getContext().getAuthentication();
        if (currentLogined != null && currentLogined.isAuthenticated() && !authenticationTrustResolver.isRememberMe(currentLogined)) {
            log.info("### 当前Session已登录拦截 [{}]", currentLogined.toString());
            return currentLogined;
        }
        if (authentication.isAuthenticated()) {
            log.info("### 取消认证，Authentication已经认证成功 [{}]", authentication.toString());
            return authentication;
        }
        log.info("### 开始验证用户[{}]", authentication.toString());
        if (!(authentication instanceof UserLoginToken)) {
            throw new BadCredentialsException("不支持的登录信息");
        }
        UserLoginToken userLoginToken = (UserLoginToken) authentication;
        if (StringUtils.isBlank(userLoginToken.getUsername())) {
            throw new BadCredentialsException("用户名不能为空");
        }
        if (StringUtils.isBlank(userLoginToken.getPassword())) {
            throw new BadCredentialsException("密码不能为空");
        }
        // 查询帐号信息
        boolean cacheWasUsed = true;
        UserDetails loadedUser = this.userCache.getUserFromCache(userLoginToken.getUsername());
        if (loadedUser == null) {
            cacheWasUsed = false;
            try {
                loadedUser = retrieveUser(userLoginToken.getUsername(), userLoginToken.getLoginType());
            } catch (UsernameNotFoundException notFound) {
                log.info("### 用户不存在[username={}]", userLoginToken.getUsername());
                throw notFound;
            }
            log.info("### 已查询到用户信息[{}]", loadedUser.toString());
        }
        // 验证帐号信息
        try {
            preAuthenticationChecks.check(loadedUser);
            mainAuthenticationChecks(userLoginToken, loadedUser);
        } catch (AuthenticationException exception) {
            if (cacheWasUsed) {
                // 缓存中的数据不一定准确，如果使用缓存数据身份认证异常，则直接使用数据库的数据
                cacheWasUsed = false;
                // 检查此处的 hideUserNotFoundExceptions 是否需要判断
                loadedUser = retrieveUser(userLoginToken.getUsername(), userLoginToken.getLoginType());
                preAuthenticationChecks.check(loadedUser);
                mainAuthenticationChecks(userLoginToken, loadedUser);
            } else {
                throw exception;
            }
        }
        postAuthenticationChecks.check(loadedUser);
        log.info("### 用户认证成功 [{}]", loadedUser.toString());
        // 帐号信息存入缓存
        if (!cacheWasUsed) {
            this.userCache.putUserInCache(loadedUser);
        }
        // 返回认证成功的 Authentication
        Authentication successAuthentication = createSuccessAuthentication(userLoginToken, loadedUser);
        // 登录并发控制
        concurrentLogin(loadedUser.getUsername());
        return successAuthentication;
    }

    /**
     * 是否支持验证此身份类型
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return (UserLoginToken.class.isAssignableFrom(authentication));
    }

    /**
     * 查询用户
     */
    private UserDetails retrieveUser(String username, String loginType) {
        if (!UserLoginToken.LoginType_Username.equals(loginType) && !UserLoginToken.LoginType_Telephone.equals(loginType)) {
            throw new BadLoginTypeException("不支持当前登录类型，" + loginType);
        }
        UserDetails loadedUser;
        try {
            loadedUser = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException notFound) {
            throw notFound;
        } catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        }
        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException("验证服务内部异常");
        }
        return loadedUser;
    }

    /**
     * 真正的密码验证
     */
    private void mainAuthenticationChecks(UserLoginToken userLoginToken, UserDetails loadedUser) throws AuthenticationException {
        if (userLoginToken.getUsername() == null) {
            throw new BadCredentialsException("用户名不能为空");
        }
        if (UserLoginToken.LoginType_Username.equals(userLoginToken.getLoginType())) {
            // 用户密码需要AES对称加解密 网络密文传输
            userLoginToken.setPassword(loginPasswordCryptoService.reqAesDecrypt(userLoginToken.getPassword()));
            // 用户名、密码校验
            if (!userLoginToken.getUsername().equals(loadedUser.getUsername())
                    || !bCryptPasswordEncoder.matches(userLoginToken.getPassword(), loadedUser.getPassword())) {
                log.info("### 用户名密码验证失败 [{}]", userLoginToken.toString());
                throw new BadCredentialsException("用户名密码验证失败");
            }
            log.info("### 用户名密码验证成功 [{}]", userLoginToken.toString());
        } else if (UserLoginToken.LoginType_Telephone.equals(userLoginToken.getLoginType())) {
            // TODO 手机号、验证码校验 (模拟)
            if (!userLoginToken.getUsername().equals(AuthenticationUtils.getLoginUserDetails(loadedUser).getUser().getTelephone())
                    || !"123456".equals(userLoginToken.getPassword())) {
                log.info("### 短信验证码验证失败 [{}]", userLoginToken.toString());
                throw new BadCredentialsException("短信验证码验证失败");
            }
            log.info("### 短信验证码验证成功 [{}]", userLoginToken.toString());
        } else {
            throw new BadCredentialsException("不支持的登录类型");
        }
    }

    /**
     * 创建认证成功的Authentication
     */
    private Authentication createSuccessAuthentication(UserLoginToken userLoginToken, UserDetails user) {
        UserLoginToken result = new UserLoginToken(user);
        result.setLoginType(userLoginToken.getLoginType());
        result.setUsername(userLoginToken.getUsername());
        result.setPassword(userLoginToken.getPassword());
        result.setDetails(userLoginToken.getDetails());
        return result;
    }

    /**
     * 并发登录处理
     */
    private void concurrentLogin(String username) {
        if (concurrentLoginCount <= 0) {
            return;
        }
        Set<String> ketSet = redisTemplate.keys(generateKeyService.getJwtTokenPatternKey(username));
        if (ketSet != null && ketSet.size() >= concurrentLoginCount) {
            if (notAllowAfterLogin) {
                throw new ConcurrentLoginException("并发登录数量超限");
            }
            // 删除之前登录的用户
            List<String> list = ketSet.stream().sorted().collect(Collectors.toList());
            int delCount = list.size() - concurrentLoginCount + 1;
            for (int i = 0; i < delCount; i++) {
                String jwtTokenKey = list.get(i);
                redisTemplate.delete(jwtTokenKey);
            }
        }
    }
}
