package org.clever.security.embed.extend;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.PasswordRecoverySupportClient;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.PasswordEmailRecoveryConfig;
import org.clever.security.embed.config.internal.PasswordRecoveryConfig;
import org.clever.security.embed.config.internal.PasswordSmsRecoveryConfig;
import org.clever.security.embed.utils.HttpServletResponseUtils;
import org.clever.security.embed.utils.PathFilterUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 密码找回过滤器
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020-12-16 22:27 <br/>
 */
@Slf4j
public class PasswordRecoveryFilter extends GenericFilterBean {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    private final PasswordRecoverySupportClient passwordRecoverySupportClient;

    public PasswordRecoveryFilter(SecurityConfig securityConfig, PasswordRecoverySupportClient passwordRecoverySupportClient) {
        this.securityConfig = securityConfig;
        this.passwordRecoverySupportClient = passwordRecoverySupportClient;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            log.warn("[clever-security]仅支持HTTP服务器");
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        boolean enablePasswordRecovery = enablePasswordRecovery();
        boolean isPasswordRecovery = false;
        try {
            if (enablePasswordRecovery && PathFilterUtils.isPasswordRecoveryRequest(httpRequest, securityConfig)) {
                // 密码找回请求
                isPasswordRecovery = true;
            } else if (enablePasswordRecovery && PathFilterUtils.isPasswordRecoverySmsValidateCodeRequest(httpRequest, securityConfig)) {
                // 密码找回 - 短信验证码
                isPasswordRecovery = true;
            } else if (enablePasswordRecovery && PathFilterUtils.isPasswordRecoveryEmailValidateCodeRequest(httpRequest, securityConfig)) {
                // 密码找回 - 邮箱验证码
                isPasswordRecovery = true;
            }
        } catch (Exception e) {
            isPasswordRecovery = true;
            log.error("密码找回处理失败", e);
            HttpServletResponseUtils.sendJson(httpRequest, httpResponse, HttpServletResponseUtils.getHttpStatus(e), e);
        }
        if (isPasswordRecovery) {
            return;
        }
        // 不是密码找回相关请求
        chain.doFilter(request, response);
    }

    /**
     * 是否启用了密码找回功能
     */
    protected boolean enablePasswordRecovery() {
        PasswordRecoveryConfig passwordRecovery = securityConfig.getPasswordRecovery();
        if (passwordRecovery == null) {
            return false;
        }
        PasswordSmsRecoveryConfig smsRecovery = passwordRecovery.getSmsRecovery();
        PasswordEmailRecoveryConfig emailRecovery = passwordRecovery.getEmailRecovery();
        return (smsRecovery != null && smsRecovery.isEnable()) || (emailRecovery != null && emailRecovery.isEnable());
    }
}
