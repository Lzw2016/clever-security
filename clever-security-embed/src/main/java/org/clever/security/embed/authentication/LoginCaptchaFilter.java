package org.clever.security.embed.authentication;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.embed.config.SecurityConfig;
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
 * 创建时间：2020/12/13 10:35 <br/>
 */
@Slf4j
public class LoginCaptchaFilter extends GenericFilterBean {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;

    public LoginCaptchaFilter(SecurityConfig securityConfig) {
        Assert.notNull(securityConfig, "权限系统配置对象(SecurityConfig)不能为null");
        this.securityConfig = securityConfig;
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
        if (!PathFilterUtils.isLoginCaptchaPath(httpRequest, securityConfig)) {
            // 不是获取登录图片验证码请求
            chain.doFilter(request, response);
            return;
        }
        // TODO 发送图片验证码
    }
}
