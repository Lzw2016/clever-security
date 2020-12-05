package org.clever.security.embed.collect;

import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;

/**
 * 收集登录信息
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 14:14 <br/>
 */
public interface LoginDataCollect extends Ordered {
    /**
     * 是否支持收集当前用户登录信息
     *
     * @param securityConfig 权限系统配置
     * @param request        请求对象
     * @return 返回true表示支持搜集
     */
    boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request);

    /***
     * 收集登录请求数据
     * @param securityConfig 权限系统配置
     * @param request           请求对象
     * @return 登录数据对象
     */
    AbstractUserLoginReq collectLoginData(SecurityConfig securityConfig, HttpServletRequest request);
}
