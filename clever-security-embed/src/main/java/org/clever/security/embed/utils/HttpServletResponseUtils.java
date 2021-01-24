package org.clever.security.embed.utils;

import org.clever.common.exception.BusinessException;
import org.clever.common.model.response.ErrorResponse;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.security.embed.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/02 20:42 <br/>
 */
public class HttpServletResponseUtils {
    /**
     * 发送数据到客户端(200状态码)
     *
     * @param response 响应对象
     * @param data     响应数据
     */
    public static void sendJson(HttpServletResponse response, Object data, HttpStatus httpStatus) throws IOException {
        if (response.isCommitted()) {
            return;
        }
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        if (data != null) {
            response.getWriter().print(JacksonMapper.getInstance().toJson(data));
            response.getWriter().flush();
        }
    }

    /**
     * 发送数据到客户端(200状态码)
     *
     * @param response 响应对象
     * @param data     响应数据
     */
    public static void sendJson(HttpServletResponse response, Object data) throws IOException {
        sendJson(response, data, HttpStatus.OK);
    }

    /**
     * 发送异常到客户端
     *
     * @param request    请求对象
     * @param response   响应对象
     * @param httpStatus 响应状态
     * @param e          异常信息
     */
    public static void sendJson(HttpServletRequest request, HttpServletResponse response, HttpStatus httpStatus, Throwable e) throws IOException {
        if (response.isCommitted()) {
            return;
        }
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setException(e.getClass().getName());
        errorResponse.setError(e.getMessage());
        if (e instanceof AuthenticationException
                || e instanceof AuthorizationException
                || e instanceof LoginException
                || e instanceof LogoutException
                || e instanceof RegisterException
                || e instanceof PasswordRecoveryException
                || e instanceof ChangeBindException
                || e instanceof UpdatePasswordException
                || e instanceof BusinessException) {
            errorResponse.setMessage(e.getMessage());
        } else {
            errorResponse.setMessage("服务器内部错误");
        }
        errorResponse.setStatus(httpStatus.value());
        errorResponse.setTimestamp(new Date());
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().print(JacksonMapper.getInstance().toJson(errorResponse));
        response.getWriter().flush();
    }

    /**
     * 重定向到指定地址
     *
     * @param response 响应对象
     * @param location 重定向地址
     */
    public static void redirect(HttpServletResponse response, String location) throws IOException {
        if (response.isCommitted()) {
            return;
        }
        response.sendRedirect(location);
    }

    public static HttpStatus getHttpStatus(Throwable e) {
        if (e == null) {
            return HttpStatus.OK;
        }
        if (e instanceof AuthenticationInnerException
                || e instanceof AuthorizationInnerException
                || e instanceof LoginInnerException
                || e instanceof RegisterInnerException
                || e instanceof ChangeBindEmailInnerException
                || e instanceof ChangeBindSmsInnerException
                || e instanceof UpdatePasswordInnerException
                || e instanceof PasswordRecoveryInnerException) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        if (e instanceof AuthenticationException
                || e instanceof AuthorizationException
                || e instanceof LoginException
                || e instanceof LogoutException
                || e instanceof RegisterException
                || e instanceof PasswordRecoveryException
                || e instanceof ChangeBindException
                || e instanceof UpdatePasswordException
                || e instanceof BusinessException) {
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
