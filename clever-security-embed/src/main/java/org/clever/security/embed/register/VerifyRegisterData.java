package org.clever.security.embed.register;

import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.exception.LoginException;
import org.clever.security.embed.exception.RegisterException;
import org.clever.security.model.register.AbstractUserRegisterReq;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 16:39 <br/>
 */
public interface VerifyRegisterData extends Ordered {
    /**
     * 是否支持验证注册用户信息
     *
     * @param securityConfig 权限系统配置
     * @param request        请求对象
     * @param registerReq    注册请求参数
     * @return 返回true表示支持搜集
     */
    boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserRegisterReq registerReq);

    /**
     * 用户注册验证
     *
     * @param securityConfig 权限系统配置
     * @param request        请求对象
     * @param registerReq    注册请求参数
     * @throws LoginException 验证失败
     */
    void verify(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserRegisterReq registerReq) throws RegisterException;
}
