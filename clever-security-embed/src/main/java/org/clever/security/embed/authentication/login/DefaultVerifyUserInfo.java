package org.clever.security.embed.authentication.login;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.dto.request.DomainExistsUserReq;
import org.clever.security.dto.request.GetConcurrentLoginCountReq;
import org.clever.security.dto.request.GetUserReq;
import org.clever.security.dto.response.DomainExistsUserRes;
import org.clever.security.dto.response.GetConcurrentLoginCountRes;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.AesKeyConfig;
import org.clever.security.embed.config.internal.LoginConfig;
import org.clever.security.embed.crypto.PasswordEncoder;
import org.clever.security.embed.exception.*;
import org.clever.security.embed.utils.AesUtils;
import org.clever.security.entity.User;
import org.clever.security.model.UserInfo;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.clever.security.model.login.LoginNamePasswordReq;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/01 21:43 <br/>
 */
@Slf4j
public class DefaultVerifyUserInfo implements VerifyUserInfo {
    private final PasswordEncoder passwordEncoder;
    private final LoginSupportClient loginSupportClient;

    public DefaultVerifyUserInfo(PasswordEncoder passwordEncoder, LoginSupportClient loginSupportClient) {
        Assert.notNull(loginSupportClient, "参数passwordEncoder不能为null");
        Assert.notNull(loginSupportClient, "参数loginSupportClient不能为null");
        this.passwordEncoder = passwordEncoder;
        this.loginSupportClient = loginSupportClient;
    }

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
        verifyUserDomain(securityConfig.getDomainId(), loginConfig, userInfo);
        // 用户过期错误
        // 用户禁用错误
        verifyUserStatus(securityConfig.getDomainId(), userInfo);
        // 登录数量超过最大并发数量错误
        verifyConcurrentLoginCount(securityConfig.getDomainId(), loginConfig, userInfo);
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
                // 解密密码(请求密码加密在客户端)
                reqPassword = AesUtils.decode(loginReqAesKey.getReqPasswordAesKey(), loginReqAesKey.getReqPasswordAesIv(), reqPassword);
            }
            // 验证密码
            if (!passwordEncoder.matches(reqPassword, userInfo.getPassword())) {
                throw new BadCredentialsException(loginConfig.isHideUserNotFoundException() ? "用户名或密码错误" : "登录密码错误");
            }
        }
    }

    /**
     * 验证用户能否登录当前域
     */
    protected void verifyUserDomain(Long domainId, LoginConfig loginConfig, UserInfo userInfo) {
        DomainExistsUserReq req = new DomainExistsUserReq(domainId);
        req.setUid(userInfo.getUid());
        DomainExistsUserRes res = loginSupportClient.domainExistsUser(req);
        if (res.isExists()) {
            return;
        }
        if (loginConfig.isHideUserNotFoundException()) {
            throw new BadCredentialsException("用户名或密码错误");
        } else {
            throw new LoginNameNotFoundException("登录名不存在");
        }
    }

    /**
     * 验证用户状态
     */
    protected void verifyUserStatus(Long domainId, UserInfo userInfo) {
        // 获取用户信息
        GetUserReq req = new GetUserReq(domainId);
        req.setUid(userInfo.getUid());
        User user = loginSupportClient.getUser(req);
        if (user.getExpiredTime() != null && new Date().compareTo(user.getExpiredTime()) >= 0) {
            throw new UserExpiredException("登录账号已过期");
        }
        if (!Objects.equals(user.getEnabled(), 1)) {
            throw new UserDisabledException("登录账号被禁用");
        }
    }

    /**
     * 登录数量超过最大并发数量校验
     */
    protected void verifyConcurrentLoginCount(Long domainId, LoginConfig loginConfig, UserInfo userInfo) {
        if (loginConfig.getConcurrentLoginCount() <= 0) {
            return;
        }
        if (loginConfig.isAllowAfterLogin()) {
            return;
        }
        // 获取当前用户并发登录数量
        GetConcurrentLoginCountReq req = new GetConcurrentLoginCountReq(domainId);
        req.setUid(userInfo.getUid());
        GetConcurrentLoginCountRes res = loginSupportClient.getConcurrentLoginCount(req);
        if (res.getConcurrentLoginCount() >= loginConfig.getConcurrentLoginCount()) {
            throw new ConcurrentLoginException("当前用户并发登录次数达到上限");
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
