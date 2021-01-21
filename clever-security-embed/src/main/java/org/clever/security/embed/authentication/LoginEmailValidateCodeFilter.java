package org.clever.security.embed.authentication;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.dto.request.SendLoginValidateCodeForEmailReq;
import org.clever.security.dto.response.SendLoginValidateCodeForEmailRes;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.exception.LoginNameNotFoundException;
import org.clever.security.embed.exception.SendValidateCodeException;
import org.clever.security.embed.utils.HttpServletRequestUtils;
import org.clever.security.embed.utils.HttpServletResponseUtils;
import org.clever.security.embed.utils.PathFilterUtils;
import org.springframework.util.Assert;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 11:04 <br/>
 */
@Slf4j
public class LoginEmailValidateCodeFilter extends HttpFilter {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    private final LoginSupportClient loginSupportClient;

    public LoginEmailValidateCodeFilter(SecurityConfig securityConfig, LoginSupportClient loginSupportClient) {
        Assert.notNull(securityConfig, "权限系统配置对象(SecurityConfig)不能为null");
        Assert.notNull(loginSupportClient, "参数loginSupportClient不能为null");
        this.securityConfig = securityConfig;
        this.loginSupportClient = loginSupportClient;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!PathFilterUtils.isLoginEmailValidateCodePath(request, securityConfig)) {
            // 不是发送邮箱验证码请求
            chain.doFilter(request, response);
            return;
        }
        try {
            sendEmailValidateCode(request, response);
        } catch (Exception e) {
            log.error("发送邮箱登录验证码失败", e);
            HttpServletResponseUtils.sendJson(request, response, HttpServletResponseUtils.getHttpStatus(e), e);
        }
    }

    @SuppressWarnings("unchecked")
    protected void sendEmailValidateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> body = (Map<String, Object>) HttpServletRequestUtils.parseBodyToEntity(request, Map.class);
        if (body == null) {
            throw new SendValidateCodeException("请求参数错误");
        }
        String email = (String) body.get("email");
        if (StringUtils.isBlank(email)) {
            throw new SendValidateCodeException("邮箱不能为空");
        }
        // 发送邮箱验证码
        SendLoginValidateCodeForEmailReq req = new SendLoginValidateCodeForEmailReq(securityConfig.getDomainId());
        req.setEmail(email);
        req.setEffectiveTimeMilli((int) securityConfig.getLogin().getEmailValidateCodeLogin().getEffectiveTime().toMillis());
        req.setMaxSendNumInDay(securityConfig.getLogin().getEmailValidateCodeLogin().getMaxSendNumInDay());
        SendLoginValidateCodeForEmailRes res = loginSupportClient.sendLoginValidateCodeForEmail(req);
        if (res == null) {
            throw new LoginNameNotFoundException("当前邮箱未注册");
        }
        log.debug("邮箱登录-验证码 -> [{}] | [{}]", res.getCode(), res.getExpiredTime());
        res.setCode("******");
        HttpServletResponseUtils.sendJson(response, res);
    }
}
