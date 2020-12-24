package org.clever.security.embed.authentication.token;

import com.google.common.base.Objects;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.utils.CookieUtils;
import org.clever.common.utils.DateTimeUtils;
import org.clever.common.utils.tuples.TupleTow;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.dto.request.AddJwtTokenReq;
import org.clever.security.dto.request.GetJwtTokenReq;
import org.clever.security.dto.request.UseRefreshJwtToken;
import org.clever.security.dto.response.AddJwtTokenRes;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.TokenConfig;
import org.clever.security.embed.exception.AuthenticationException;
import org.clever.security.embed.exception.InvalidJwtTokenException;
import org.clever.security.embed.utils.JwtTokenUtils;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.JwtToken;
import org.clever.security.model.UserInfo;
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

//    private final AuthSupportClient authSupportClient;

    public DefaultVerifyJwtToken(LoginSupportClient loginSupportClient/*, AuthSupportClient authSupportClient*/) {
        Assert.notNull(loginSupportClient, "参数loginSupportClient不能为null");
        this.loginSupportClient = loginSupportClient;
//        Assert.notNull(authSupportClient, "参数authSupportClient不能为null");
//        this.authSupportClient = authSupportClient;
    }

    @Override
    public void verify(String jwtToken, String uid, Claims claims, SecurityConfig securityConfig, HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 校验JWT-Token
        verifyJwtToken(securityConfig.getDomainId(), claims, securityConfig, request, response);
        log.debug("### JWT-Token验证成功 -> uid={} | Token={}", uid, jwtToken);
    }

    protected void verifyJwtToken(Long domainId, Claims claims, SecurityConfig securityConfig, HttpServletRequest request, HttpServletResponse response) {
        TokenConfig tokenConfig = securityConfig.getTokenConfig();
        // 验证JWT-Token状态
        GetJwtTokenReq req = new GetJwtTokenReq(domainId);
        req.setId(Long.parseLong(claims.getId()));
        JwtToken jwtToken = loginSupportClient.getJwtToken(req);
        if (jwtToken == null) {
            // 无效的 Token
            throw new InvalidJwtTokenException("无效的Token");
        }
        if (!Objects.equal(jwtToken.getDisable(), EnumConstant.JwtToken_Disable_0)) {
            // 已禁用
            throw new InvalidJwtTokenException("Token已禁用");
        }
        Date now = new Date();
        if (jwtToken.getExpiredTime() != null && now.compareTo(jwtToken.getExpiredTime()) >= 0) {
            verifyRefreshToken(request, response, tokenConfig, jwtToken, domainId);
        }
    }

    protected void verifyRefreshToken(HttpServletRequest request, HttpServletResponse response, TokenConfig tokenConfig, JwtToken jwtToken, Long domainId) {
        //是否启用刷新令牌
        if (tokenConfig.isEnableRefreshToken()) {
            log.info("### JWT-Token过期解析刷新token");
            String refreshToken;
            //cookie or header
            if (tokenConfig.isUseCookie()) {
                refreshToken = CookieUtils.getCookie(request, tokenConfig.getRefreshTokenName());
            } else {
                refreshToken = request.getHeader(tokenConfig.getRefreshTokenName());
            }
            Date now = new Date();
            if (StringUtils.isBlank(refreshToken) || now.compareTo(jwtToken.getRefreshTokenExpiredTime()) >= 0) {
                //刷新Token已过期
                throw new InvalidJwtTokenException("Token已过期");
            }
            if (!Objects.equal(jwtToken.getRefreshTokenState(), EnumConstant.JwtToken_RefreshTokenState_1)) {
                //刷新Token无效
                throw new InvalidJwtTokenException("Token已过期");
            }
            if (Objects.equal(refreshToken, jwtToken.getRefreshToken())) {
                log.info("### 刷新token");
                //todo  userinfo的获取?  loaduserinfo还是?
                UserInfo userInfo = new UserInfo();
                userInfo.setUid("123456");
                //--------
                final TupleTow<String, Claims> tokenInfo = JwtTokenUtils.createJwtToken(tokenConfig, userInfo, null);
                AddJwtTokenRes res = addJwtToken(tokenInfo, domainId, tokenConfig, userInfo);
                //修改旧token数据状态
                UseRefreshJwtToken req = new UseRefreshJwtToken(domainId);
                req.setId(jwtToken.getId());
                req.setRefreshCreateTokenId(res.getId());
                loginSupportClient.useRefreshJwtToken(req);
                // 将刷新生成的JWT-Token写入客户端
                if (tokenConfig.isUseCookie()) {
                    int maxAge = DateTimeUtils.pastSeconds(new Date(), tokenInfo.getValue2().getExpiration()) + (60 * 3);
                    CookieUtils.setCookie(response, "/", tokenConfig.getJwtTokenName(), tokenInfo.getValue1(), maxAge);
                } else {
                    response.addHeader(tokenConfig.getJwtTokenName(), tokenInfo.getValue1());
                }
                return;
            }
        }
        // 已过期
        throw new InvalidJwtTokenException("Token已过期");
    }

    protected AddJwtTokenRes addJwtToken(TupleTow<String, Claims> tokenInfo, Long domainId, TokenConfig tokenConfig, UserInfo userInfo) {
        AddJwtTokenReq req = new AddJwtTokenReq(domainId);
        if (tokenConfig.isEnableRefreshToken()) {
            String refreshToken = JwtTokenUtils.createRefreshToken(userInfo);
            req.setRefreshToken(refreshToken);
            req.setRefreshTokenExpiredTime(DateTimeUtils.addDays(new Date(), (int) tokenConfig.getRefreshTokenValidity().toDays()));
        }
        req.setId(Long.parseLong(tokenInfo.getValue2().getId()));
        req.setUid(userInfo.getUid());
        req.setToken(tokenInfo.getValue1());
        req.setExpiredTime(tokenInfo.getValue2().getExpiration());
        AddJwtTokenRes res = loginSupportClient.addJwtToken(req);
        log.debug("### JWT-Token保存成功 -> Id={} | ExpiredTime={}", res.getId(), res.getExpiredTime());
        return res;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
