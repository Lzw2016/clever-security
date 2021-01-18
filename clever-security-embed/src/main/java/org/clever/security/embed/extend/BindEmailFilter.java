package org.clever.security.embed.extend;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.common.utils.validator.ValidatorFactoryUtils;
import org.clever.security.Constant;
import org.clever.security.client.BindSupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.BindEmailConfig;
import org.clever.security.embed.context.SecurityContextHolder;
import org.clever.security.embed.exception.AuthorizationInnerException;
import org.clever.security.embed.exception.ChangeBindEmailInnerException;
import org.clever.security.embed.exception.ChangeBindEmailValidateCodeException;
import org.clever.security.embed.utils.HttpServletRequestUtils;
import org.clever.security.embed.utils.HttpServletResponseUtils;
import org.clever.security.embed.utils.PathFilterUtils;
import org.clever.security.model.SecurityContext;
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
    protected void sendEmailCaptcha(HttpServletResponse response) throws IOException {
        BindEmailConfig bindEmail = securityConfig.getBindEmail();
        if (bindEmail == null || !bindEmail.isEnable()) {
            throw new UnsupportedOperationException("未启用邮箱换绑");
        }
        if (!bindEmail.isNeedCaptcha()) {
            throw new UnsupportedOperationException("邮箱换绑不需要图片验证码");
        }
        GetBindEmailCaptchaReq req = new GetBindEmailCaptchaReq(securityConfig.getDomainId());
        req.setEffectiveTimeMilli((int) bindEmail.getCaptchaEffectiveTime().toMillis());
        GetBindEmailCaptchaRes res = bindSupportClient.getBindEmailCaptcha(req);
        log.debug("邮箱换绑-验证码 -> [{}] | [{}]", res.getCode(), res.getExpiredTime());
        response.addHeader(Constant.Login_Captcha_Digest_Response_Header, res.getDigest());
        byte[] image = EncodeDecodeUtils.decodeBase64(res.getCodeContent());
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.getOutputStream().write(image);
        response.setStatus(HttpStatus.OK.value());
    }

    //发送邮箱验证码
    protected void sendEmailValidateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BindEmailConfig bindEmail = securityConfig.getBindEmail();
        if (bindEmail == null || !bindEmail.isEnable()) {
            throw new UnsupportedOperationException("未启用邮箱换绑");
        }
        SendBindEmailValidateCodeReq req = HttpServletRequestUtils.parseBodyToEntity(request, SendBindEmailValidateCodeReq.class);
        if (req == null) {
            throw new BusinessException("请求数据解析异常(邮箱换绑发送手机验证码)");
        }
        req.setDomainId(securityConfig.getDomainId());
        req.setEffectiveTimeMilli((int) bindEmail.getEffectiveTime().toMillis());
        req.setMaxSendNumInDay(bindEmail.getMaxSendNumInDay());
        try {
            ValidatorFactoryUtils.getValidatorInstance().validate(req);
        } catch (Exception e) {
            throw new BusinessException("请求数据校验失败(邮箱换绑发送邮箱验证码)", e);
        }
        // 校验图形验证码
        if (bindEmail.isNeedCaptcha()) {
            if (StringUtils.isBlank(req.getCaptcha()) || StringUtils.isBlank(req.getCaptchaDigest())) {
                throw new BusinessException("验证码不能为空");
            }
            VerifyBindEmailCaptchaReq verifyBindEmailCaptchaReq = new VerifyBindEmailCaptchaReq(securityConfig.getDomainId());
            verifyBindEmailCaptchaReq.setCaptcha(req.getCaptcha());
            verifyBindEmailCaptchaReq.setCaptchaDigest(req.getCaptchaDigest());
            VerifyBindEmailCaptchaRes res = bindSupportClient.verifyBindEmailCaptcha(verifyBindEmailCaptchaReq);
            if (res == null) {
                throw new ChangeBindEmailInnerException("验证图片验证码失败");
            } else if (!res.isSuccess()) {
                throw new ChangeBindEmailValidateCodeException(res.isExpired() ? "图片验证码已失效" : "图片验证码错误");
            }
        }
        // 发送邮箱验证码
        SendBindEmailValidateCodeRes res = bindSupportClient.sendBindEmailValidateCode(req);
        if (res == null) {
            throw new BusinessException("邮箱验证码发送失败");
        }
        log.debug("邮箱换绑-邮箱验证码 -> [{}] | [{}]", res.getCode(), res.getExpiredTime());
        res.setCode("******");
        HttpServletResponseUtils.sendJson(response, res);
    }

    //更换邮箱绑定
    protected void changeBindEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityContext securityContext = SecurityContextHolder.getContext(request);
        if (securityContext == null) {
            throw new AuthorizationInnerException("获取SecurityContext失败");
        }
        BIndEmailReq req = HttpServletRequestUtils.parseBodyToEntity(request, BIndEmailReq.class);
        if (req == null) {
            throw new BusinessException("请求数据解析异常(邮箱换绑)");
        }
        try {
            ValidatorFactoryUtils.getValidatorInstance().validate(req);
        } catch (Exception e) {
            throw new BusinessException("请求数据校验失败(邮箱换绑)", e);
        }
        VerifyBindEmailValidateCodeReq verifyBindEmailValidateCodeReq = new VerifyBindEmailValidateCodeReq(securityConfig.getDomainId());
        verifyBindEmailValidateCodeReq.setCode(req.getCode());
        verifyBindEmailValidateCodeReq.setCodeDigest(req.getCodeDigest());
        verifyBindEmailValidateCodeReq.setEmail(req.getEmail());
        VerifyBindEmailValidateCodeRes emailValidateCodeRes = bindSupportClient.verifyBindEmailValidateCode(verifyBindEmailValidateCodeReq);
        if (emailValidateCodeRes == null) {
            throw new ChangeBindEmailInnerException("验证邮箱验证码失败");
        } else if (!emailValidateCodeRes.isSuccess()) {
            throw new ChangeBindEmailValidateCodeException(emailValidateCodeRes.isExpired() ? "邮箱验证码已失效" : "邮箱验证码错误");
        }
        ChangeBindEmailReq changeBindEmailReq = new ChangeBindEmailReq(securityConfig.getDomainId());
        changeBindEmailReq.setUid(securityContext.getUserInfo().getUid());
        changeBindEmailReq.setEmail(req.getEmail());
        ChangeBindEmailRes res = bindSupportClient.changeBindEmail(changeBindEmailReq);
        if (res == null) {
            throw new ChangeBindEmailInnerException("邮箱换绑失败");
        } else if (!res.isSuccess()) {
            throw new BusinessException(res.getMessage());
        }
        HttpServletResponseUtils.sendJson(response, res);
    }
}
