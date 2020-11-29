package org.clever.security.embed.collect;

import org.clever.security.embed.config.SecurityConfig;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/06/30 17:51 <br/>
 */
public interface IUserRegisterDataCollect extends Ordered {
    /**
     * 是否支持收集当前注册信息
     *
     * @param securityConfig 系统授权配置
     * @param request        请求对象
     * @return 返回true表示支持搜集
     */
    boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request);
}
