package org.clever.security.jwt.handler;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.security.dto.response.AccessDeniedRes;
import org.clever.security.jwt.config.SecurityConfig;
import org.clever.security.jwt.utils.HttpRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拒绝访问处理器
 * 作者： lzw<br/>
 * 创建时间：2018-03-18 15:09 <br/>
 */
@SuppressWarnings("Duplicates")
@Component
@Getter
@Slf4j
public class UserAccessDeniedHandler implements AccessDeniedHandler {

    @SuppressWarnings("FieldCanBeLocal")
    private final String defaultRedirectUrl = "/403.html";

    @Autowired
    private SecurityConfig securityConfig;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 不需要跳转
        if (securityConfig.getForbiddenNeedForward() != null && !securityConfig.getForbiddenNeedForward()) {
            sendJsonData(response);
            return;
        }
        // 需要跳转 - 判断请求是否需要跳转
        if (HttpRequestUtils.isJsonResponse(request)) {
            sendJsonData(response);
            return;
        }
        // 需要跳转
        String url = securityConfig.getForbiddenForwardPage();
        if (StringUtils.isBlank(url)) {
            url = defaultRedirectUrl;
        }
        request.setAttribute(WebAttributes.ACCESS_DENIED_403, accessDeniedException);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        RequestDispatcher dispatcher = request.getRequestDispatcher(url);
        dispatcher.forward(request, response);
    }

    /**
     * 直接返回Json数据
     */
    private void sendJsonData(HttpServletResponse response) throws IOException {
        String json = JacksonMapper.nonEmptyMapper().toJson(new AccessDeniedRes("没有访问权限"));
        if (!response.isCommitted()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(json);
        }
    }
}
