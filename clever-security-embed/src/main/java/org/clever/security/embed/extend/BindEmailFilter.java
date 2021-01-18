package org.clever.security.embed.extend;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.BindSupportClient;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.BindEmailConfig;
import org.clever.security.embed.utils.HttpServletResponseUtils;
import org.clever.security.embed.utils.PathFilterUtils;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/18 22:14 <br/>
 */
@Slf4j
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
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            log.warn("[clever-security]仅支持HTTP服务器");
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        boolean changeBindEmail = false;
        try {
            if (PathFilterUtils.isChangeBindEmailCaptchaPathRequest(httpRequest, securityConfig)) {
                // 邮箱换绑 - 图片验证码
                changeBindEmail = true;
                sendEmailCaptcha(httpResponse);
            } else if (PathFilterUtils.isChangeBindEmailValidateCodeRequest(httpRequest, securityConfig)
                    && securityConfig.getBindEmail().isEnable()) {
                // 邮箱换绑 - 邮箱验证码
                changeBindEmail = true;
                sendEmailValidateCode(httpRequest, httpResponse);
            } else if (PathFilterUtils.isChangeBindEmailRequest(httpRequest, securityConfig)) {
                // 邮箱换绑 - 绑定邮箱
                changeBindEmail = true;
                changeBindEmail(httpRequest, httpResponse);
            }
        } catch (Exception e) {
            changeBindEmail = true;
            log.error("邮箱换绑处理失败", e);
            HttpServletResponseUtils.sendJson(httpRequest, httpResponse, HttpServletResponseUtils.getHttpStatus(e), e);
        }
        if (changeBindEmail) {
            return;
        }
        // 不是绑定邮箱相关请求
        chain.doFilter(request, response);
    }

    //发送邮箱图片验证码
    protected void sendEmailCaptcha(HttpServletResponse httpResponse) {
        BindEmailConfig bindEmail = securityConfig.getBindEmail();
        if (bindEmail == null || !bindEmail.isEnable()) {
            throw new UnsupportedOperationException("未启用邮箱换绑");
        }
        if (!bindEmail.isNeedCaptcha()) {
            throw new UnsupportedOperationException("邮箱换绑不需要图片验证码");
        }
        //todo 服务层调度
    }

    //发送邮箱验证码
    protected void sendEmailValidateCode(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        BindEmailConfig bindEmail = securityConfig.getBindEmail();
        if (bindEmail == null || !bindEmail.isEnable()) {
            throw new UnsupportedOperationException("未启用邮箱换绑");
        }
        //todo 服务层调度
    }

    //更换邮箱绑定
    protected void changeBindEmail(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        //todo 服务层调度
    }
}
