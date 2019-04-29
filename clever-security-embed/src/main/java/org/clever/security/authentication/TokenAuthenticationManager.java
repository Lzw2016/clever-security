package org.clever.security.authentication;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.exception.BusinessException;
import org.clever.security.LoginModel;
import org.clever.security.client.ManageByUserClient;
import org.clever.security.client.UserClient;
import org.clever.security.config.SecurityConfig;
import org.clever.security.config.model.LoginConfig;
import org.clever.security.exception.BadLoginTypeException;
import org.clever.security.exception.ConcurrentLoginException;
import org.clever.security.model.LoginUserDetails;
import org.clever.security.repository.RedisJwtRepository;
import org.clever.security.service.GlobalUserDetailsService;
import org.clever.security.service.RequestCryptoService;
import org.clever.security.token.JwtAccessToken;
import org.clever.security.token.SecurityContextToken;
import org.clever.security.token.login.BaseLoginToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
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
 * 登录Token认证 BaseLoginToken
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-03-15 17:41 <br/>
 */
@Component
@Slf4j
public class TokenAuthenticationManager implements AuthenticationProvider {

    /**
     * 登入模式
     */
    private LoginModel loginModel;
    /**
     * 同一个用户并发登录次数限制(-1表示不限制)
     */
    private final int concurrentLoginCount;
    /**
     * 同一个用户并发登录次数达到最大值之后，是否不允许之后的登录(false 之后登录的把之前登录的挤下来)
     */
    private final boolean notAllowAfterLogin;
    /**
     * 密码处理
     */
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    /**
     * 加载用户信息
     */
    @Autowired
    private GlobalUserDetailsService globalUserDetailsService;
    /**
     * 读取系统用户
     */
    @Autowired
    private UserClient userClient;
    /**
     * 新增系统用户
     */
    @Autowired
    private ManageByUserClient manageByUserClient;
    /**
     * 从第三方加载用户信息支持
     */
    @Autowired(required = false)
    private List<LoadThirdUser> loadThirdUserList;
    /**
     * 帐号校验(密码认证之前)
     */
    @Autowired
    @Qualifier("DefaultPreAuthenticationChecks")
    private UserDetailsChecker preAuthenticationChecks;
    /**
     * 帐号校验(密码认证之后)
     */
    @Autowired
    @Qualifier("DefaultPostAuthenticationChecks")
    private UserDetailsChecker postAuthenticationChecks;
    /**
     * 请求参数加密/解密
     */
    @Autowired
    private RequestCryptoService requestCryptoService;
    /**
     * JwtToken数据存取操作组件
     */
    @Autowired
    private RedisJwtRepository redisJwtRepository;
    /**
     * Token认证登录
     */
    @Autowired
    private List<AuthenticationLoginToken> authenticationLoginTokenList;
    /**
     * 用户数据本地缓存
     */
    private UserCache userCache = new NullUserCache();
    /**
     * 解析授权信息
     */
    private AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();

    public TokenAuthenticationManager(SecurityConfig securityConfig) {
        LoginConfig login = securityConfig.getLogin();
        if (login == null) {
            throw new BusinessException("未配置Login配置");
        }
        concurrentLoginCount = login.getConcurrentLoginCount();
        notAllowAfterLogin = login.getNotAllowAfterLogin();
        loginModel = securityConfig.getLoginModel();
    }

    /**
     * 是否支持验证此身份类型
     */
    @Override
    public boolean supports(Class<?> authentication) {
        if (BaseLoginToken.class.isAssignableFrom(authentication)) {
            if (authenticationLoginTokenList == null) {
                log.warn("未注入Token认证登录组件");
            } else {
                for (AuthenticationLoginToken authenticationLoginToken : authenticationLoginTokenList) {
                    if (authenticationLoginToken.supports(authentication)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 认证用户登录
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication currentLogined = SecurityContextHolder.getContext().getAuthentication();
        if (currentLogined != null && currentLogined.isAuthenticated() && !authenticationTrustResolver.isRememberMe(currentLogined)) {
            log.info("### 当前用户已登录拦截 [{}]", currentLogined.toString());
            return currentLogined;
        }
        if (authentication.isAuthenticated()) {
            log.info("### 取消认证，Authentication已经认证成功 [{}]", authentication.toString());
            return authentication;
        }
        log.info("### 开始验证用户[{}]", authentication.toString());
        if (!(authentication instanceof BaseLoginToken)) {
            throw new BadLoginTypeException(String.format("不支持的登录信息,%s | %s", authentication.getClass(), authentication.toString()));
        }
        // 获取对应的认证组件
        AuthenticationLoginToken authenticationLoginToken = null;
        for (AuthenticationLoginToken tmp : authenticationLoginTokenList) {
            if (tmp.supports(authentication.getClass())) {
                authenticationLoginToken = tmp;
                break;
            }
        }
        if (authenticationLoginToken == null) {
            throw new InternalAuthenticationServiceException("找不到Token认证登录组件");
        }
        BaseLoginToken loginToken = (BaseLoginToken) authentication;
        authenticationLoginToken.preAuthenticationCheck(loginToken);
        // 查询帐号信息
        boolean cacheWasUsed = true;
        UserDetails loadedUser = this.userCache.getUserFromCache(loginToken.getName());
        if (loadedUser == null) {
            cacheWasUsed = false;
            try {
                loadedUser = retrieveUser(loginToken);
            } catch (UsernameNotFoundException notFound) {
                log.info("### 用户不存在[username={}]", loginToken.getName());
                throw notFound;
            }
            log.info("### 已查询到用户信息[{}]", loadedUser.toString());
        }
        // 验证帐号信息
        try {
            preAuthenticationChecks.check(loadedUser);
            authenticationLoginToken.mainAuthenticationChecks(loginToken, loadedUser, requestCryptoService, bCryptPasswordEncoder);
        } catch (AuthenticationException exception) {
            if (cacheWasUsed) {
                // 缓存中的数据不一定准确，如果使用缓存数据身份认证异常，则直接使用数据库的数据
                cacheWasUsed = false;
                // 检查此处的 hideUserNotFoundExceptions 是否需要判断
                loadedUser = retrieveUser(loginToken);
                preAuthenticationChecks.check(loadedUser);
                authenticationLoginToken.mainAuthenticationChecks(loginToken, loadedUser, requestCryptoService, bCryptPasswordEncoder);
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
        Authentication successAuthentication = createSuccessAuthentication(loginToken, loadedUser);
        // JwtToken 并发登录控制
        if (LoginModel.jwt.equals(loginModel)) {
            concurrentLogin(loadedUser.getUsername());
        }
        return successAuthentication;
    }

    /**
     * 查询用户
     */
    private UserDetails retrieveUser(BaseLoginToken loginToken) {
        UserDetails loadedUser = null;
        try {
            // 从第三方加载用户数据
            LoadThirdUser loadThirdUser = null;
            if (loadThirdUserList != null) {
                for (LoadThirdUser tmp : loadThirdUserList) {
                    if (tmp.supports(loginToken)) {
                        loadThirdUser = tmp;
                        break;
                    }
                }
            }
            if (loadThirdUser != null) {
                loadedUser = loadThirdUser.loadUser(loginToken, userClient, manageByUserClient);
            }
            if (loadedUser == null) {
                loadedUser = globalUserDetailsService.loadUserByUsername(loginToken.getName());
            }
        } catch (UsernameNotFoundException notFound) {
            throw notFound;
        } catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        }
        return loadedUser;
    }

    /**
     * 创建认证成功的Authentication
     */
    private SecurityContextToken createSuccessAuthentication(BaseLoginToken loginToken, UserDetails userDetails) {
        LoginUserDetails loginUserDetails;
        if (!(userDetails instanceof LoginUserDetails)) {
            throw new InternalAuthenticationServiceException(String.format("加载的用户类型不正确,%s | %s", userDetails.getClass(), userDetails.toString()));
        }
        loginUserDetails = (LoginUserDetails) userDetails;
        return new SecurityContextToken(loginToken, loginUserDetails);
    }

    /**
     * JwtToken 并发登录控制
     */
    private void concurrentLogin(String username) {
        if (concurrentLoginCount <= 0) {
            return;
        }
        Set<String> ketSet = redisJwtRepository.getJwtTokenPatternKey(username);
        if (ketSet != null && ketSet.size() >= concurrentLoginCount) {
            if (notAllowAfterLogin) {
                throw new ConcurrentLoginException("并发登录数量超限");
            }
            // 删除之前登录的Token 和 刷新令牌
            List<String> list = ketSet.stream().sorted().collect(Collectors.toList());
            int delCount = list.size() - concurrentLoginCount + 1;
            for (int i = 0; i < delCount; i++) {
                String jwtTokenKey = list.get(i);
                JwtAccessToken jwtAccessToken = null;
                try {
                    jwtAccessToken = redisJwtRepository.getJwtTokenByKey(jwtTokenKey);
                } catch (Throwable ignored) {
                }
                assert jwtAccessToken != null;
                redisJwtRepository.deleteJwtToken(jwtAccessToken);
            }
        }
    }
}
