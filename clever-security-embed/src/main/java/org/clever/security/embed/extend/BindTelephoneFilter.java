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
import org.clever.security.embed.config.internal.AesKeyConfig;
import org.clever.security.embed.config.internal.BindTelephoneConfig;
import org.clever.security.embed.context.SecurityContextHolder;
import org.clever.security.embed.exception.AuthorizationInnerException;
import org.clever.security.embed.exception.BadCredentialsException;
import org.clever.security.embed.exception.ChangeBindSmsInnerException;
import org.clever.security.embed.exception.ChangeBindSmsValidateCodeException;
import org.clever.security.embed.utils.AesUtils;
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
    protected void sendSmsCaptcha(HttpServletResponse response) throws IOException {
        BindTelephoneConfig bindTelephone = securityConfig.getBindTelephone();
        if (bindTelephone == null || !bindTelephone.isEnable()) {
            throw new UnsupportedOperationException("未启用手机号换绑");
        }
        if (!bindTelephone.isNeedCaptcha()) {
            throw new UnsupportedOperationException("手机号换绑不需要图片验证码");
        }
        GetBindSmsCaptchaReq req = new GetBindSmsCaptchaReq(securityConfig.getDomainId());
        req.setEffectiveTimeMilli((int) bindTelephone.getCaptchaEffectiveTime().toMillis());
        GetBindSmsCaptchaRes res = bindSupportClient.getBindSmsCaptcha(req);
        log.debug("手机换绑-验证码 -> [{}] | [{}]", res.getCode(), res.getExpiredTime());
        response.addHeader(Constant.Login_Captcha_Digest_Response_Header, res.getDigest());
        byte[] image = EncodeDecodeUtils.decodeBase64(res.getCodeContent());
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.getOutputStream().write(image);
        response.setStatus(HttpStatus.OK.value());
    }

    //发送手机号验证码
    protected void sendSmsValidateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityContext securityContext = SecurityContextHolder.getContext(request);
        if (securityContext == null) {
            throw new AuthorizationInnerException("获取SecurityContext失败");
        }
        BindTelephoneConfig bindTelephone = securityConfig.getBindTelephone();
        if (bindTelephone == null || !bindTelephone.isEnable()) {
            throw new UnsupportedOperationException("未启用手机号换绑");
        }
        SendBindSmsValidateCodeReq req = HttpServletRequestUtils.parseBodyToEntity(request, SendBindSmsValidateCodeReq.class);
        if (req == null) {
            throw new BusinessException("请求数据解析异常(手机换绑发送手机验证码)");
        }
        req.setUid(securityContext.getUserInfo().getUid());
        req.setDomainId(securityConfig.getDomainId());
        req.setEffectiveTimeMilli((int) bindTelephone.getEffectiveTime().toMillis());
        req.setMaxSendNumInDay(bindTelephone.getMaxSendNumInDay());
        try {
            ValidatorFactoryUtils.getValidatorInstance().validate(req);
        } catch (Exception e) {
            throw new BusinessException("请求数据校验失败(手机换绑发送短信验证码)", e);
        }
        // 校验图形验证码
        if (bindTelephone.isNeedCaptcha()) {
            if (StringUtils.isBlank(req.getCaptcha()) || StringUtils.isBlank(req.getCaptchaDigest())) {
                throw new BusinessException("验证码不能为空");
            }
            VerifyBindSmsCaptchaReq verifyBindSmsCaptchaReq = new VerifyBindSmsCaptchaReq(securityConfig.getDomainId());
            verifyBindSmsCaptchaReq.setCaptcha(req.getCaptcha());
            verifyBindSmsCaptchaReq.setCaptchaDigest(req.getCaptchaDigest());
            VerifyBindSmsCaptchaRes res = bindSupportClient.verifyBindSmsCaptcha(verifyBindSmsCaptchaReq);
            if (res == null) {
                throw new ChangeBindSmsInnerException("验证图片验证码失败");
            } else if (!res.isSuccess()) {
                throw new ChangeBindSmsValidateCodeException(res.isExpired() ? "图片验证码已失效" : "图片验证码错误");
            }
        }
        // 发送短信验证码
        SendBindSmsValidateCodeRes res = bindSupportClient.sendBindSmsValidateCode(req);
        if (res == null) {
            throw new BusinessException("短信验证码发送失败");
        }
        log.debug("手机号换绑-短信验证码 -> [{}] | [{}]", res.getCode(), res.getExpiredTime());
        res.setCode("******");
        HttpServletResponseUtils.sendJson(response, res);
    }

    //更换手机号绑定
    protected void changeBindSms(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityContext securityContext = SecurityContextHolder.getContext(request);
        if (securityContext == null) {
            throw new AuthorizationInnerException("获取SecurityContext失败");
        }
        BindSmsReq req = HttpServletRequestUtils.parseBodyToEntity(request, BindSmsReq.class);
        if (req == null) {
            throw new BusinessException("请求数据解析异常(手机号换绑)");
        }
        try {
            ValidatorFactoryUtils.getValidatorInstance().validate(req);
        } catch (Exception e) {
            throw new BusinessException("请求数据校验失败(手机号换绑)", e);
        }
        String password = req.getPassWord();
        AesKeyConfig reqAesKey = securityConfig.getReqAesKey();
        if (reqAesKey != null && reqAesKey.isEnable()) {
            try {
                password = AesUtils.decode(reqAesKey.getReqPasswordAesKey(), reqAesKey.getReqPasswordAesIv(), password);
                req.setPassWord(password);
            } catch (Exception e) {
                throw new BadCredentialsException("密码需要加密传输", e);
            }
        }
        VerifyBindSmsValidateCodeReq verifyBindSmsValidateCodeReq = new VerifyBindSmsValidateCodeReq(securityConfig.getDomainId());
        verifyBindSmsValidateCodeReq.setCode(req.getCode());
        verifyBindSmsValidateCodeReq.setCodeDigest(req.getCodeDigest());
        verifyBindSmsValidateCodeReq.setTelephone(req.getTelephone());
        VerifyBindSmsValidateCodeRes smsValidateCodeRes = bindSupportClient.verifyBindSmsValidateCode(verifyBindSmsValidateCodeReq);
        if (smsValidateCodeRes == null) {
            throw new ChangeBindSmsInnerException("验证短信验证码失败");
        } else if (!smsValidateCodeRes.isSuccess()) {
            throw new ChangeBindSmsValidateCodeException(smsValidateCodeRes.isExpired() ? "短信验证码已失效" : (smsValidateCodeRes.isPassWord() ? "短信验证码错误" : "密码错误"));
        }
        ChangeBindSmsReq changeBindSmsReq = new ChangeBindSmsReq(securityConfig.getDomainId());
        changeBindSmsReq.setUid(securityContext.getUserInfo().getUid());
        changeBindSmsReq.setPassWord(password);
        changeBindSmsReq.setTelephone(req.getTelephone());
        ChangeBindSmsRes res = bindSupportClient.changeBindSms(changeBindSmsReq);
        if (res == null) {
            throw new ChangeBindSmsInnerException("手机号换绑失败");
        } else if (!res.isSuccess()) {
            throw new BusinessException(res.getMessage());
        }
        HttpServletResponseUtils.sendJson(response, res);
    }
}
