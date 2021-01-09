package org.clever.security.embed.register;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.RegisterSupportClient;
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
 * 创建时间：2021/01/09 22:21 <br/>
 */
@Slf4j
public class RegisterCaptchaFilter extends GenericFilterBean {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    /**
     * 注册支持对象
     */
    private final RegisterSupportClient registerSupportClient;

    public RegisterCaptchaFilter(SecurityConfig securityConfig, RegisterSupportClient registerSupportClient) {
        Assert.notNull(securityConfig, "权限系统配置对象(SecurityConfig)不能为null");
        Assert.notNull(registerSupportClient, "参数registerSupportClient不能为null");
        this.securityConfig = securityConfig;
        this.registerSupportClient = registerSupportClient;
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
        // UserRegisterConfig register =  securityConfig.getRegister();
        if (PathFilterUtils.isRegisterCaptchaRequest(httpRequest, securityConfig)) {
            // 登录名注册-验证码

            return;
        } else if (PathFilterUtils.isRegisterCaptchaRequest(httpRequest, securityConfig)) {
            // 短信注册-图片验证码

        } else if (PathFilterUtils.isRegisterCaptchaRequest(httpRequest, securityConfig)) {
            // 短信注册-短信验证码

        } else if (PathFilterUtils.isRegisterCaptchaRequest(httpRequest, securityConfig)) {
            // 邮箱注册-图片验证码

        } else if (PathFilterUtils.isRegisterCaptchaRequest(httpRequest, securityConfig)) {
            // 邮箱注册-邮箱验证码

        } else {
            // 不是获取注册验证码相关请求
            chain.doFilter(request, response);
        }
    }
}
