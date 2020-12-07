package org.clever.security.embed.collect;

import org.clever.security.LoginType;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.LoginConfig;
import org.clever.security.embed.utils.HttpServletRequestUtils;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.clever.security.model.login.ScanCodeReq;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 收集“浏览器扫描码/用户登录的Token”登录数据
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:05 <br/>
 */
public class ScanCodeReqCollect extends AbstractLoginDataCollect {
    @Override
    public boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request) {
        String loginType = request.getParameter(AbstractUserLoginReq.LoginType_ParamName);
        LoginType loginTypeEnum = LoginType.lookup(loginType);
        if (loginTypeEnum == null) {
            ScanCodeReq req = getScanCodeReq(securityConfig, request);
            return req != null;
        }
        return Objects.equals(LoginType.ScanCode.getId(), loginTypeEnum.getId());
    }

    @Override
    public AbstractUserLoginReq collectLoginData(SecurityConfig securityConfig, HttpServletRequest request) {
        return getScanCodeReq(securityConfig, request);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    protected ScanCodeReq getScanCodeReq(SecurityConfig securityConfig, HttpServletRequest request) {
        LoginConfig login = securityConfig.getLogin();
        ScanCodeReq req = HttpServletRequestUtils.parseBodyToEntity(request, ScanCodeReq.class);
        if (req == null && login.isPostOnly()) {
            return null;
        }
        if (req == null) {
            req = new ScanCodeReq();
        }
        // 收集基础数据
        collectBaseDataByParameter(req, request);
        // 收集当前登录类型数据
        if (req.getBrowseScanCode() == null) {
            String browseScanCode = request.getParameter(ScanCodeReq.BrowseScanCode_ParamName);
            if (browseScanCode != null) {
                req.setBrowseScanCode(browseScanCode);
            }
        }
        if (req.getLoginToken() == null) {
            String loginToken = request.getParameter(ScanCodeReq.LoginToken_ParamName);
            if (loginToken != null) {
                req.setLoginToken(loginToken);
            }
        }
        if (req.getBrowseScanCode() == null && req.getLoginToken() == null) {
            return null;
        }
        return req;
    }
}

