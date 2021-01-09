package org.clever.security.embed.register;

import org.clever.security.embed.config.SecurityConfig;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 用户注册过滤器
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020-12-16 22:25 <br/>
 */
public class UserRegisterFilter extends GenericFilterBean {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;

    public UserRegisterFilter(SecurityConfig securityConfig) {
        Assert.notNull(securityConfig, "权限系统配置对象(SecurityConfig)不能为null");
        this.securityConfig = securityConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // TODO 用户注册
        chain.doFilter(request, response);
    }
}
