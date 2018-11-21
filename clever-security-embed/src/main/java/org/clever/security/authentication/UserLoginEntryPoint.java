package org.clever.security.authentication;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.model.response.ErrorResponse;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.security.config.SecurityConfig;
import org.clever.security.utils.HttpRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 未登录访问时的处理
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-03-16 11:20 <br/>
 */
@SuppressWarnings("Duplicates")
@Component
@Slf4j
public class UserLoginEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private SecurityConfig securityConfig;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.info("### 未登录访问拦截，访问地址[{}] {}", request.getMethod(), request.getRequestURL().toString());
        if (!securityConfig.getNotLoginNeedForward() || HttpRequestUtils.isJsonResponse(request)) {
            // 返回401错误
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=utf-8");
            // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("您还未登录，请先登录", authException, HttpServletResponse.SC_UNAUTHORIZED, request.getRequestURI());
            response.getWriter().print(JacksonMapper.nonEmptyMapper().toJson(errorResponse));
            return;
        }
        // 跳转到登录页面
        String redirectUrl = securityConfig.getLogin().getLoginPage();
        if (StringUtils.isBlank(redirectUrl)) {
            redirectUrl = "/index.html";
        }
        response.sendRedirect(redirectUrl);
    }
}
