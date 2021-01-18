package org.clever.security.embed.extend;

import org.clever.security.client.ResetPasswordSupportClient;
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
 * 创建时间：2021/01/18 18:09 <br/>
 */
public class ResetPasswordFilter extends GenericFilterBean {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    private final ResetPasswordSupportClient resetPasswordSupportClient;

    public ResetPasswordFilter(SecurityConfig securityConfig, ResetPasswordSupportClient resetPasswordSupportClient) {
        Assert.notNull(securityConfig, "权限系统配置对象(SecurityConfig)不能为null");
        Assert.notNull(resetPasswordSupportClient, "参数resetPasswordSupportClient不能为null");
        this.securityConfig = securityConfig;
        this.resetPasswordSupportClient = resetPasswordSupportClient;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // TODO 设置/修改密码
    }
}
