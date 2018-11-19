package org.clever.security.jwt.handler;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.security.dto.response.JwtLoginRes;
import org.clever.security.dto.response.UserRes;
import org.clever.security.jwt.AttributeKeyConstant;
import org.clever.security.jwt.model.JwtToken;
import org.clever.security.jwt.service.GenerateKeyService;
import org.clever.security.jwt.service.JWTTokenService;
import org.clever.security.jwt.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 自定义登录成功处理类
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-03-16 11:06 <br/>
 */
@SuppressWarnings("Duplicates")
@Component
@Slf4j
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();
    @Autowired
    private JWTTokenService jwtTokenService;
    @Autowired
    private GenerateKeyService generateKeyService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        requestCache.removeRequest(request, response);
        clearAuthenticationAttributes(request);
        // 登录成功保存 JWT Token
        JwtToken jwtToken = jwtTokenService.createToken(authentication, false);
        String jwtTokenKey = generateKeyService.getJwtTokenKey(jwtToken.getClaims());
        if (jwtToken.getClaims().getExpiration() == null) {
            redisTemplate.opsForValue().set(jwtTokenKey, jwtToken);
        } else {
            long timeout = jwtToken.getClaims().getExpiration().getTime() - System.currentTimeMillis();
            redisTemplate.opsForValue().set(jwtTokenKey, jwtToken, timeout, TimeUnit.MILLISECONDS);
        }
        log.info("### 已保存 JWT Token");
        // 登录成功 - 返回JSon数据
        sendJsonData(response, authentication, jwtToken);
    }

    /**
     * 直接返回Json数据
     */
    private void sendJsonData(HttpServletResponse response, Authentication authentication, JwtToken jwtToken) throws IOException {
        response.setHeader(GenerateKeyService.JwtTokenHeaderKey, jwtToken.getToken());
        UserRes userRes = AuthenticationUtils.getUserRes(authentication);
        String json = JacksonMapper.nonEmptyMapper().toJson(new JwtLoginRes(true, "登录成功", userRes, jwtToken.getToken()));
        log.info("### 登录成功不需要跳转 -> [{}]", json);
        if (!response.isCommitted()) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(json);
        }
    }

    /**
     * 清除Session中的异常信息
     */
    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        session.removeAttribute(AttributeKeyConstant.Login_Captcha_Session_Key);
        session.removeAttribute(AttributeKeyConstant.Login_Fail_Count_Session_Key);
    }
}
