package org.clever.security.jwt.handler;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.security.dto.response.JwtLoginRes;
import org.clever.security.dto.response.UserRes;
import org.clever.security.jwt.model.JwtToken;
import org.clever.security.jwt.repository.LoginFailCountRepository;
import org.clever.security.jwt.repository.RedisJwtRepository;
import org.clever.security.jwt.service.GenerateKeyService;
import org.clever.security.jwt.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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

    private RequestCache requestCache = new NullRequestCache();

    @Autowired
    private RedisJwtRepository redisJwtRepository;
    @Autowired
    private LoginFailCountRepository loginFailCountRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        requestCache.removeRequest(request, response);
        clearAuthenticationAttributes(request, authentication.getName());
        // 登录成功保存 JwtToken RefreshToken
        JwtToken jwtToken = redisJwtRepository.saveJwtToken(AuthenticationUtils.getUserLoginToken(authentication));
        redisJwtRepository.saveSecurityContext(new SecurityContextImpl(authentication));
        log.info("### 已保存 JWT Token 和 SecurityContext");
        // 登录成功 - 返回JSon数据
        sendJsonData(response, authentication, jwtToken);
    }

    /**
     * 直接返回Json数据
     */
    private void sendJsonData(HttpServletResponse response, Authentication authentication, JwtToken jwtToken) throws IOException {
        response.setHeader(GenerateKeyService.JwtTokenHeaderKey, jwtToken.getToken());
        UserRes userRes = AuthenticationUtils.getUserRes(authentication);
        String json = JacksonMapper.nonEmptyMapper().toJson(
                new JwtLoginRes(
                        true,
                        "登录成功",
                        userRes,
                        jwtToken.getToken(),
                        jwtToken.getRefreshToken()
                )
        );
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
    private void clearAuthenticationAttributes(HttpServletRequest request, String username) {
        loginFailCountRepository.deleteLoginFailCount(username);
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
