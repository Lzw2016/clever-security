package org.clever.security.embed.authentication;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.security.Constant;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.dto.request.CreateLoginScanCodeReq;
import org.clever.security.dto.request.GetScanCodeLoginInfoReq;
import org.clever.security.dto.response.CreateLoginScanCodeRes;
import org.clever.security.dto.response.GetScanCodeLoginInfoRes;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.exception.ScanCodeLoginException;
import org.clever.security.embed.utils.HttpServletRequestUtils;
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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 12:24 <br/>
 */
@Slf4j
public class ScanCodeLoginFilter extends GenericFilterBean {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    private final LoginSupportClient loginSupportClient;

    public ScanCodeLoginFilter(SecurityConfig securityConfig, LoginSupportClient loginSupportClient) {
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
        if (PathFilterUtils.isGetScanCodeLoginPath(httpRequest, securityConfig)) {
            // 获取扫码登录二维码
            try {
                getLoginScanCode(httpRequest, httpResponse);
            } catch (Exception e) {
                log.error("获取扫码登录二维码失败", e);
                HttpServletResponseUtils.sendJson(httpRequest, httpResponse, HttpServletResponseUtils.getHttpStatus(e), e);
            }
        } else if (PathFilterUtils.isScanCodeStatePath(httpRequest, securityConfig)) {
            // 获取登录二维码状态
            try {
                getLoginScanCodeState(httpRequest, httpResponse);
            } catch (Exception e) {
                log.error("获取登录二维码状态失败", e);
                HttpServletResponseUtils.sendJson(httpRequest, httpResponse, HttpServletResponseUtils.getHttpStatus(e), e);
            }
        } else {
            // 不是扫码登录相关请求
            chain.doFilter(request, response);
        }
    }

    protected void getLoginScanCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CreateLoginScanCodeReq req = new CreateLoginScanCodeReq(securityConfig.getDomainId());
        req.setExpiredTime((int) securityConfig.getLogin().getScanCodeLogin().getExpiredTime().toMillis());
        req.setConfirmExpiredTime((int) securityConfig.getLogin().getScanCodeLogin().getConfirmExpiredTime().toMillis());
        req.setGetTokenExpiredTime((int) securityConfig.getLogin().getScanCodeLogin().getGetTokenExpiredTime().toMillis());
        CreateLoginScanCodeRes res = loginSupportClient.createLoginScanCode(req);
        byte[] image = EncodeDecodeUtils.decodeBase64(res.getScanCodeContent());
        response.addHeader(Constant.Login_Scan_Code_Response_Header, res.getScanCode());
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.getOutputStream().write(image);
        response.setStatus(HttpStatus.OK.value());
    }

    protected void getLoginScanCodeState(HttpServletRequest request, HttpServletResponse response) throws IOException {
        GetScanCodeLoginInfoReq req = HttpServletRequestUtils.parseBodyToEntity(request, GetScanCodeLoginInfoReq.class);
        if (req == null) {
            throw new ScanCodeLoginException("请求参数错误");
        }
        if (StringUtils.isBlank(req.getScanCode())) {
            throw new ScanCodeLoginException("参数scanCode不能为空");
        }
        req.setDomainId(securityConfig.getDomainId());
        GetScanCodeLoginInfoRes res = loginSupportClient.getScanCodeLoginInfo(req);
        if (res == null) {
            throw new ScanCodeLoginException("二维码不存在或者已过期");
        }
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("scanCode", res.getScanCode());
        map.put("scanCodeState", res.getScanCodeState());
        HttpServletResponseUtils.sendJson(response, map);
    }
}
