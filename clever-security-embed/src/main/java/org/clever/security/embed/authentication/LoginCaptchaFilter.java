package org.clever.security.embed.authentication;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.security.Constant;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.dto.request.GetLoginCaptchaReq;
import org.clever.security.dto.response.GetLoginCaptchaRes;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.utils.HttpServletResponseUtils;
import org.clever.security.embed.utils.PathFilterUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    /**
     * 登录支持对象
     */
    private final LoginSupportClient loginSupportClient;

    public LoginCaptchaFilter(SecurityConfig securityConfig, LoginSupportClient loginSupportClient) {
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
        if (!PathFilterUtils.isLoginCaptchaPath(httpRequest, securityConfig)) {
            // 不是获取登录图片验证码请求
            chain.doFilter(request, response);
            return;
        }
        try {
            // 发送图片验证码
            GetLoginCaptchaReq req = new GetLoginCaptchaReq(securityConfig.getDomainId());
            GetLoginCaptchaRes res = loginSupportClient.getLoginCaptcha(req);
            log.debug("登录图片验证码 -> [{}] | [{}]", res.getCode(), res.getExpiredTime());
            httpResponse.addHeader(Constant.Login_Captcha_Digest_Response_Header, res.getDigest());
            byte[] image = EncodeDecodeUtils.decodeBase64(res.getCodeContent());
            httpResponse.setContentType(MediaType.IMAGE_PNG_VALUE);
            httpResponse.getOutputStream().write(image);
            httpResponse.setStatus(HttpStatus.OK.value());
        } catch (Exception e) {
            log.error("获取图片验证码失败", e);
            HttpServletResponseUtils.sendJson(httpRequest, httpResponse, HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }
}
