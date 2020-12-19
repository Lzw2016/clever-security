package org.clever.security.embed.authentication;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.dto.request.SendLoginValidateCodeForSmsReq;
import org.clever.security.dto.response.SendLoginValidateCodeForSmsRes;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.utils.HttpServletResponseUtils;
import org.clever.security.embed.utils.PathFilterUtils;
import org.springframework.http.HttpStatus;
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
 * 创建时间：2020/12/13 11:04 <br/>
 */
@Slf4j
public class LoginSmsValidateCodeFilter extends GenericFilterBean {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    private final LoginSupportClient loginSupportClient;

    public LoginSmsValidateCodeFilter(SecurityConfig securityConfig, LoginSupportClient loginSupportClient) {
        Assert.notNull(securityConfig, "权限系统配置对象(SecurityConfig)不能为null");
        Assert.notNull(loginSupportClient, "参数loginSupportClient不能为null");
        this.securityConfig = securityConfig;
        this.loginSupportClient = loginSupportClient;
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
        if (!PathFilterUtils.isLoginSmsValidateCodePath(httpRequest, securityConfig)) {
            // 不是发送短信验证码请求
            chain.doFilter(request, response);
            return;
        }
        // 发送短信验证码
        SendLoginValidateCodeForSmsReq req = new SendLoginValidateCodeForSmsReq(securityConfig.getDomainId());
        // TODO 设置手机号
        req.setTelephone("");
        req.setEffectiveTimeMilli((int) securityConfig.getLogin().getSmsValidateCodeLogin().getEffectiveTime().toMillis());
        req.setMaxSendNumInDay(securityConfig.getLogin().getSmsValidateCodeLogin().getMaxSendNumInDay());
        try {
            SendLoginValidateCodeForSmsRes res = loginSupportClient.sendLoginValidateCodeForSms(req);
            HttpServletResponseUtils.sendJson(httpResponse, res);
        } catch (Exception e) {
            log.error("发送短信登录验证码失败", e);
            HttpServletResponseUtils.sendJson(httpRequest, httpResponse, HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }
}
