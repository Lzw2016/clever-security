package org.clever.security.embed.authentication.token;

import com.google.common.base.Objects;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.dto.request.GetJwtTokenReq;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.exception.AuthenticationException;
import org.clever.security.embed.exception.InvalidJwtTokenException;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.JwtToken;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/14 21:22 <br/>
 */
@Slf4j
public class DefaultVerifyJwtToken implements VerifyJwtToken {
    private final LoginSupportClient loginSupportClient;

    public DefaultVerifyJwtToken(LoginSupportClient loginSupportClient) {
        Assert.notNull(loginSupportClient, "参数loginSupportClient不能为null");
        this.loginSupportClient = loginSupportClient;
    }

    @Override
    public void verify(String jwtToken, String uid, Claims claims, SecurityConfig securityConfig, HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 校验JWT-Token
        verifyJwtToken(securityConfig.getDomainId(), claims);
        log.debug("### JWT-Token验证成功 -> uid={} | Token={}", uid, jwtToken);
    }

    protected void verifyJwtToken(Long domainId, Claims claims) {
        // 验证JWT-Token状态
        GetJwtTokenReq req = new GetJwtTokenReq(domainId);
        req.setId(Long.parseLong(claims.getId()));
        JwtToken jwtToken = loginSupportClient.getJwtToken(req);
        if (jwtToken == null) {
            // 无效的 Token
            throw new InvalidJwtTokenException("无效的Token");
        }
        Date now = new Date();
        if (jwtToken.getExpiredTime() != null && now.compareTo(jwtToken.getExpiredTime()) >= 0) {
            // 已过期
            throw new InvalidJwtTokenException("Token已过期");
        }
        if (!Objects.equal(jwtToken.getDisable(), EnumConstant.JwtToken_Disable_0)) {
            // 已禁用
            throw new InvalidJwtTokenException("Token已禁用");
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
