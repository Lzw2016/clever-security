package org.clever.security.embed.extend;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.validator.BaseValidatorUtils;
import org.clever.common.utils.validator.ValidatorFactoryUtils;
import org.clever.security.client.UpdatePasswordSupportClient;
import org.clever.security.dto.request.InitPasswordReq;
import org.clever.security.dto.request.UpdatePasswordReq;
import org.clever.security.dto.response.InitPasswordRes;
import org.clever.security.dto.response.UpdatePasswordRes;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.AesKeyConfig;
import org.clever.security.embed.config.internal.UpdatePasswordConfig;
import org.clever.security.embed.context.SecurityContextHolder;
import org.clever.security.embed.exception.AuthorizationInnerException;
import org.clever.security.embed.exception.BadCredentialsException;
import org.clever.security.embed.exception.InitPasswordInnerException;
import org.clever.security.embed.exception.UpdatePasswordInnerException;
import org.clever.security.embed.utils.AesUtils;
import org.clever.security.embed.utils.HttpServletRequestUtils;
import org.clever.security.embed.utils.HttpServletResponseUtils;
import org.clever.security.embed.utils.PathFilterUtils;
import org.clever.security.model.SecurityContext;
import org.springframework.util.Assert;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/18 18:09 <br/>
 */
@Slf4j
public class UpdatePasswordFilter extends HttpFilter {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    private final UpdatePasswordSupportClient updatePasswordSupportClient;

    public UpdatePasswordFilter(SecurityConfig securityConfig, UpdatePasswordSupportClient updatePasswordSupportClient) {
        Assert.notNull(securityConfig, "权限系统配置对象(SecurityConfig)不能为null");
        Assert.notNull(updatePasswordSupportClient, "参数updatePasswordSupportClient不能为null");
        this.securityConfig = securityConfig;
        this.updatePasswordSupportClient = updatePasswordSupportClient;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean changePassword = false;
        UpdatePasswordConfig updatePassword = securityConfig.getUpdatePassword();
        if (updatePassword == null || !updatePassword.isEnable()) {
            throw new UnsupportedOperationException("未启设置密码");
        }
        try {
            if (PathFilterUtils.isInitPassWordRequest(request, securityConfig)) {
                //初始化密码
                changePassword = true;
                initPassword(request, response);
            } else if (PathFilterUtils.isUpdatePassWordRequest(request, securityConfig)) {
                //修改密码
                changePassword = true;
                updatePassword(request, response);
            }
        } catch (Exception e) {
            changePassword = true;
            log.error("邮箱换绑处理失败", e);
            HttpServletResponseUtils.sendJson(request, response, HttpServletResponseUtils.getHttpStatus(e), e);
        }
        if (changePassword) {
            return;
        }
        chain.doFilter(request, response);
    }

    //初始化密码
    protected void initPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityContext securityContext = SecurityContextHolder.getContext(request);
        if (securityContext == null) {
            throw new AuthorizationInnerException("获取SecurityContext失败");
        }
        InitPasswordReq req = HttpServletRequestUtils.parseBodyToEntity(request, InitPasswordReq.class);
        if (req == null) {
            throw new BusinessException("请求数据解析异常(初始化密码)");
        }
        req.setUid(securityContext.getUserInfo().getUid());
        try {
            BaseValidatorUtils.validateThrowException(ValidatorFactoryUtils.getValidatorInstance(), req);
        } catch (Exception e) {
            throw new BusinessException("请求数据校验失败(初始化密码)", e);
        }
        InitPasswordRes res = updatePasswordSupportClient.initPassword(req);
        if (res == null) {
            throw new InitPasswordInnerException("设置密码失败");
        } else if (!res.isSuccess()) {
            throw new BusinessException(res.getMessage());
        }
        HttpServletResponseUtils.sendJson(response, res);
    }

    //修改密码
    protected void updatePassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityContext securityContext = SecurityContextHolder.getContext(request);
        if (securityContext == null) {
            throw new AuthorizationInnerException("获取SecurityContext失败");
        }
        UpdatePasswordReq req = HttpServletRequestUtils.parseBodyToEntity(request, UpdatePasswordReq.class);
        if (req == null) {
            throw new BusinessException("请求数据解析异常(修改密码)");
        }
        req.setUid(securityContext.getUserInfo().getUid());
        try {
            BaseValidatorUtils.validateThrowException(ValidatorFactoryUtils.getValidatorInstance(), req);
        } catch (Exception e) {
            throw new BusinessException("请求数据校验失败(修改密码)", e);
        }
        String oldPassword = req.getOldPassword();
        String newPassword = req.getNewPassword();
        AesKeyConfig reqAesKey = securityConfig.getReqAesKey();
        if (reqAesKey != null && reqAesKey.isEnable()) {
            // 解密密码(请求密码加密在客户端)
            try {
                oldPassword = AesUtils.decode(reqAesKey.getReqPasswordAesKey(), reqAesKey.getReqPasswordAesIv(), oldPassword);
                req.setOldPassword(oldPassword);
                newPassword = AesUtils.decode(reqAesKey.getReqPasswordAesKey(), reqAesKey.getReqPasswordAesIv(), newPassword);
                req.setNewPassword(newPassword);
            } catch (Exception e) {
                throw new BadCredentialsException("密码需要加密传输", e);
            }
        }
        UpdatePasswordRes res = updatePasswordSupportClient.updatePassword(req);
        if (res == null) {
            throw new UpdatePasswordInnerException("修改密码失败");
        } else if (!res.isSuccess()) {
            throw new BusinessException(res.getMessage());
        }
        HttpServletResponseUtils.sendJson(response, res);
    }
}
