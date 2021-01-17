package org.clever.security.embed.extend;

import org.clever.security.client.BindSupportClient;
import org.clever.security.embed.config.SecurityConfig;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/18 22:14 <br/>
 */
public class BindEmailFilter extends GenericFilterBean {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    private final BindSupportClient bindSupportClient;

    public BindEmailFilter(SecurityConfig securityConfig, BindSupportClient bindSupportClient) {
        Assert.notNull(securityConfig, "权限系统配置对象(SecurityConfig)不能为null");
        Assert.notNull(bindSupportClient, "参数bindSupportClient不能为null");
        this.securityConfig = securityConfig;
        this.bindSupportClient = bindSupportClient;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // TODO 邮箱绑定/换绑
        chain.doFilter(request, response);
    }
}
