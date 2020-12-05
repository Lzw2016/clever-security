package org.clever.security.embed.utils;

import org.clever.common.model.response.ErrorResponse;
import org.clever.common.utils.mapper.JacksonMapper;
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
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        if (data != null) {
            response.getWriter().print(JacksonMapper.getInstance().toJson(data));
            response.getWriter().flush();
        }
        response.setStatus(httpStatus.value());
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
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setException(e.getClass().getName());
        errorResponse.setError(e.getMessage());
        errorResponse.setMessage("服务器内部错误");
        errorResponse.setStatus(httpStatus.value());
        errorResponse.setTimestamp(new Date());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().print(JacksonMapper.getInstance().toJson(errorResponse));
        response.getWriter().flush();
        response.setStatus(httpStatus.value());
    }

    /**
     * 重定向到指定地址
     *
     * @param response 响应对象
     * @param location 重定向地址
     */
    public static void redirect(HttpServletResponse response, String location) throws IOException {
        response.sendRedirect(location);
    }
}
