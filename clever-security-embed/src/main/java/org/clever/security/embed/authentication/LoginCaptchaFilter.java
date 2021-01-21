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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 10:35 <br/>
 */
@Slf4j
public class LoginCaptchaFilter extends HttpFilter {
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
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!PathFilterUtils.isLoginCaptchaPath(request, securityConfig)) {
            // 不是获取登录图片验证码请求
            chain.doFilter(request, response);
            return;
        }
        try {
            // 发送图片验证码
            GetLoginCaptchaReq req = new GetLoginCaptchaReq(securityConfig.getDomainId());
            req.setEffectiveTimeMilli((int) securityConfig.getLogin().getLoginCaptcha().getEffectiveTime().toMillis());
            GetLoginCaptchaRes res = loginSupportClient.getLoginCaptcha(req);
            log.debug("登录图片验证码 -> [{}] | [{}]", res.getCode(), res.getExpiredTime());
            response.addHeader(Constant.Login_Captcha_Digest_Response_Header, res.getDigest());
            byte[] image = EncodeDecodeUtils.decodeBase64(res.getCodeContent());
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            response.getOutputStream().write(image);
            response.setStatus(HttpStatus.OK.value());
        } catch (Exception e) {
            log.error("获取图片验证码失败", e);
            HttpServletResponseUtils.sendJson(request, response, HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }
}
