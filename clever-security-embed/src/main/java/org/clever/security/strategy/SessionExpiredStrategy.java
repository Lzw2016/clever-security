package org.clever.security.strategy;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.model.response.ErrorResponse;
import org.clever.common.utils.mapper.JacksonMapper;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Session 过期时的处理
 * 作者： lzw<br/>
 * 创建时间：2018-09-20 15:00 <br/>
 */
@Data
@Slf4j
public class SessionExpiredStrategy implements SessionInformationExpiredStrategy {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * 需要跳转
     */
    private Boolean needRedirect = false;

    /**
     * 跳转Url
     */
    private String destinationUrl = "/login.html";

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException {
        HttpServletRequest request = event.getRequest();
        HttpServletResponse response = event.getResponse();
        if (needRedirect) {
            redirectStrategy.sendRedirect(request, response, destinationUrl);
            return;
        }
        if (!response.isCommitted()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setPath(request.getPathInfo());
            errorResponse.setTimestamp(new Date());
            errorResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            errorResponse.setError("当前登录已过期(可能是因为同一用户尝试多次并发登录)");
            errorResponse.setMessage("当前登录已过期(可能是因为同一用户尝试多次并发登录)");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JacksonMapper.getInstance().toJson(errorResponse));
        }
    }
}
