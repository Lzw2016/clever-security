package org.clever.security.embed.authentication.login;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.LoginConfig;
import org.clever.security.embed.exception.LoginException;
import org.clever.security.embed.exception.LoginInnerException;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录数据基础校验
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/12/01 20:57 <br/>
 */
@Slf4j
public class BasicVerifyLoginData implements VerifyLoginData {
    @Override
    public boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserLoginReq loginReq) {
        return loginReq != null;
    }

    @Override
    public void verify(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserLoginReq loginReq) throws LoginException {
        // 验证码错误
        // 登录数量超过最大并发数量错误
        LoginConfig login = securityConfig.getLogin();
        //todo 登录失败处理
        if (login.isNeedCaptcha()) {
            //count登录失败次数
            int count = 0;
            if (count >= login.getNeedCaptchaByLoginFailedCount()) {
                //todo 验证码验证
                String loginCaptcha = "系统颁发的正确验证码";
                if (!loginReq.getLoginCaptcha().equals(loginCaptcha)) {
                    throw new LoginInnerException("登录验证码错误");
                }
            }
        }
        // 登录数量超过最大并发数量错误
        if (!login.isAllowRepeatLogin()) {
            //todo isLogin当前用户是否已登录
            boolean isLogin = false;
            if (isLogin) {
                throw new LoginInnerException("当前用户未退出登录");
            }
        } else {
            if (!login.isAllowAfterLogin()) {
                //todo count当前登录用户并发登录次数
                int count = 0;
                if (count >= login.getConcurrentLoginCount()) {
                    throw new LoginInnerException("当前用户并发登录次数达到上限");
                }
            }
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
