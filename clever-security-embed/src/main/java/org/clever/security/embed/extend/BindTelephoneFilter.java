package org.clever.security.embed.extend;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.BindSupportClient;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.BindTelephoneConfig;
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
 * 创建时间：2020/12/18 22:12 <br/>
 */
@Slf4j
public class BindTelephoneFilter extends GenericFilterBean {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    private final BindSupportClient bindSupportClient;

    public BindTelephoneFilter(SecurityConfig securityConfig, BindSupportClient bindSupportClient) {
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
        boolean changeBindSms = false;
        try {
            if (PathFilterUtils.isChangeBindSmsCaptchaPathRequest(httpRequest, securityConfig)) {
                // 手机换绑 - 图片验证码
                changeBindSms = true;
                sendSmsCaptcha(httpResponse);
            } else if (PathFilterUtils.isChangeBindSmsValidateCodeRequest(httpRequest, securityConfig)) {
                // 手机换绑 - 短信验证码
                changeBindSms = true;
                sendSmsValidateCode(httpRequest, httpResponse);
            } else if (PathFilterUtils.isChangeBindSmsRequest(httpRequest, securityConfig)) {
                // 手机换绑 - 手机号换绑
                changeBindSms = true;
                changeBindSms(httpRequest, httpResponse);
            }
        } catch (Exception e) {
            changeBindSms = true;
            log.error("手机号换绑处理失败", e);
            HttpServletResponseUtils.sendJson(httpRequest, httpResponse, HttpServletResponseUtils.getHttpStatus(e), e);
        }
        if (changeBindSms) {
            return;
        }
        // 不是绑定手机号相关请求
        chain.doFilter(request, response);
    }

    //发送手机号图片验证码
    protected void sendSmsCaptcha(HttpServletResponse httpResponse) {
        BindTelephoneConfig bindTelephone = securityConfig.getBindTelephone();
        if (bindTelephone == null || !bindTelephone.isEnable()) {
            throw new UnsupportedOperationException("未启用手机号换绑");
        }
        if (!bindTelephone.isNeedCaptcha()) {
            throw new UnsupportedOperationException("手机号换绑不需要图片验证码");
        }
        //todo 服务层调度
    }

    //发送手机号验证码
    protected void sendSmsValidateCode(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        BindTelephoneConfig bindTelephone = securityConfig.getBindTelephone();
        if (bindTelephone == null || !bindTelephone.isEnable()) {
            throw new UnsupportedOperationException("未启用手机号换绑");
        }
        //todo 服务层调度
    }

    //更换手机号绑定
    protected void changeBindSms(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        //todo 服务层调度
    }
}
