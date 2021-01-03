package org.clever.security.embed.authentication;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.dto.request.BindLoginScanCodeReq;
import org.clever.security.dto.request.ConfirmLoginScanCodeReq;
import org.clever.security.dto.response.BindLoginScanCodeRes;
import org.clever.security.dto.response.ConfirmLoginScanCodeRes;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.exception.ScanCodeLoginException;
import org.clever.security.embed.utils.HttpServletRequestUtils;
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
 * 创建时间：2020/12/13 11:00 <br/>
 */
@Slf4j
public class ScanCodeLoginSupportFilter extends GenericFilterBean {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    private final LoginSupportClient loginSupportClient;

    public ScanCodeLoginSupportFilter(SecurityConfig securityConfig, LoginSupportClient loginSupportClient) {
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
        Claims claims = (Claims) request.getAttribute(AuthenticationFilter.JWT_Object_Request_Attribute);
        if (PathFilterUtils.isScanCodePath(httpRequest, securityConfig)) {
            // 扫描扫码登录二维码
            try {
                bindLoginScanCode(claims, httpRequest, httpResponse);
            } catch (Exception e) {
                log.error("扫描扫码登录二维码处理失败", e);
                HttpServletResponseUtils.sendJson(httpRequest, httpResponse, HttpServletResponseUtils.getHttpStatus(e), e);
            }
        } else if (PathFilterUtils.isScanCodeLoginConfirmPath(httpRequest, securityConfig)) {
            // 扫码登录确认登录
            try {
                confirmLoginScanCode(claims, httpRequest, httpResponse);
            } catch (Exception e) {
                log.error("扫码登录确认登录处理失败", e);
                HttpServletResponseUtils.sendJson(httpRequest, httpResponse, HttpServletResponseUtils.getHttpStatus(e), e);
            }
        } else {
            // 不是扫码登录相关请求
            chain.doFilter(request, response);
        }
    }

    protected void bindLoginScanCode(Claims claims, HttpServletRequest request, HttpServletResponse response) throws IOException {
        BindLoginScanCodeReq req = HttpServletRequestUtils.parseBodyToEntity(request, BindLoginScanCodeReq.class);
        if (req == null) {
            throw new ScanCodeLoginException("请求参数错误");
        }
        req.setDomainId(securityConfig.getDomainId());
        req.setBindTokenId(Long.parseLong(claims.getId()));
        req.setConfirmExpiredTime((int) securityConfig.getLogin().getScanCodeLogin().getConfirmExpiredTime().toMillis());
        BindLoginScanCodeRes res = loginSupportClient.bindLoginScanCode(req);
        if (res == null) {
            throw new ScanCodeLoginException("二维码不存在或者已过期");
        }
        HttpServletResponseUtils.sendJson(response, res);
    }

    protected void confirmLoginScanCode(Claims claims, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ConfirmLoginScanCodeReq req = HttpServletRequestUtils.parseBodyToEntity(request, ConfirmLoginScanCodeReq.class);
        if (req == null) {
            throw new ScanCodeLoginException("请求参数错误");
        }
        req.setDomainId(securityConfig.getDomainId());
        req.setBindTokenId(Long.parseLong(claims.getId()));
        req.setGetTokenExpiredTime((int) securityConfig.getLogin().getScanCodeLogin().getGetTokenExpiredTime().toMillis());
        ConfirmLoginScanCodeRes res = loginSupportClient.confirmLoginScanCode(req);
        if (res == null) {
            throw new ScanCodeLoginException("二维码不存在或者已过期");
        }
        HttpServletResponseUtils.sendJson(response, res);
    }
}
