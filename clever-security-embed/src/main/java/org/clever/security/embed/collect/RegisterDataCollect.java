package org.clever.security.embed.collect;

import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.model.register.AbstractUserRegisterReq;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;

/**
 * 收集注册信息
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 14:07 <br/>
 */
public interface RegisterDataCollect extends Ordered {
    /**
     * 是否支持收集当前用户注册信息
     *
     * @param securityConfig 权限系统配置
     * @param request        请求对象
     * @return 返回true表示支持搜集
     */
    boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request);

    /***
     * 收集注册请求数据
     * @param securityConfig 权限系统配置
     * @param request           请求对象
     * @return 注册数据对象
     */
    AbstractUserRegisterReq collectRegisterData(SecurityConfig securityConfig, HttpServletRequest request);
}
