package org.clever.security.embed.extend;

import org.clever.security.client.UpdatePasswordSupportClient;
import org.clever.security.embed.config.SecurityConfig;
import org.springframework.util.Assert;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/18 18:09 <br/>
 */
public class UpdatePasswordFilter extends HttpFilter {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    private final UpdatePasswordSupportClient updatePasswordSupportClient;

    public UpdatePasswordFilter(SecurityConfig securityConfig, UpdatePasswordSupportClient updatePasswordSupportClient) {
        Assert.notNull(securityConfig, "权限系统配置对象(SecurityConfig)不能为null");
        Assert.notNull(updatePasswordSupportClient, "参数updatePasswordSupportClient不能为null");
        this.securityConfig = securityConfig;
        this.updatePasswordSupportClient = updatePasswordSupportClient;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // TODO 设置/修改密码
    }
}
