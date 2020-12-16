package org.clever.security.embed.handler;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.dto.request.DisableJwtTokenReq;
import org.clever.security.embed.event.LogoutSuccessEvent;
import org.clever.security.entity.JwtToken;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/14 21:33 <br/>
 */
@Slf4j
public class DefaultLogoutSuccessHandler implements LogoutSuccessHandler {
    private final LoginSupportClient loginSupportClient;

    public DefaultLogoutSuccessHandler(LoginSupportClient loginSupportClient) {
        Assert.notNull(loginSupportClient, "参数loginSupportClient不能为null");
        this.loginSupportClient = loginSupportClient;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, LogoutSuccessEvent event) throws IOException, ServletException {
        // 禁用JWT-Token
        disableJwtToken(event.getDomainId(), event.getClaims());
    }

    protected void disableJwtToken(Long domainId, Claims claims) {
        DisableJwtTokenReq req = new DisableJwtTokenReq(domainId);
        req.setId(Long.parseLong(claims.getId()));
        req.setDisableReason("用户主动登出");
        JwtToken jwtToken = loginSupportClient.disableJwtToken(req);
        log.debug("### 登出成功 -> uid={} | JWT-Token={}", jwtToken.getUid(), jwtToken.getToken());
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
