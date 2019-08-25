package org.clever.security.utils;

import org.clever.common.utils.StringUtils;
import org.clever.common.utils.mapper.JacksonMapper;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-04-29 09:19 <br/>
 */
public class HttpResponseUtils {

    private static void sendJson(HttpServletResponse response, Object resData, int status) {
        if (!response.isCommitted()) {
            String json;
            if (resData instanceof String
                    || resData instanceof Byte
                    || resData instanceof Short
                    || resData instanceof Integer
                    || resData instanceof Float
                    || resData instanceof Long
                    || resData instanceof Double
                    || resData instanceof Boolean) {
                json = String.valueOf(resData);
            } else {
                json = JacksonMapper.getInstance().toJson(resData);
            }
            response.setStatus(status);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=utf-8");
            try {
                response.getWriter().print(json);
            } catch (IOException e) {
                throw new InternalAuthenticationServiceException("返回数据写入响应流失败", e);
            }
        }
    }

    /**
     * 写入响应Json数据
     */
    public static void sendJsonBy200(HttpServletResponse response, Object resData) {
        sendJson(response, resData, HttpServletResponse.SC_OK);
    }

    /**
     * 写入响应Json数据
     */
    public static void sendJsonBy401(HttpServletResponse response, Object resData) {
        sendJson(response, resData, HttpServletResponse.SC_UNAUTHORIZED);
    }

    /**
     * 写入响应Json数据
     */
    public static void sendJsonBy403(HttpServletResponse response, Object resData) {
        sendJson(response, resData, HttpServletResponse.SC_FORBIDDEN);
    }

    /**
     * 重定向页面(客户端跳转)
     *
     * @param redirectUrl 跳转地址
     */
    public static void sendRedirect(HttpServletResponse response, String redirectUrl) throws IOException {
        if (StringUtils.isNotBlank(redirectUrl)) {
            response.sendRedirect(redirectUrl);
        }
    }
}
