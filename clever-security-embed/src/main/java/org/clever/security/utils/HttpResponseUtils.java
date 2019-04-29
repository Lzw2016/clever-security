package org.clever.security.utils;

import org.springframework.security.authentication.InternalAuthenticationServiceException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-04-29 09:19 <br/>
 */
public class HttpResponseUtils {

    /**
     * 写入响应Json数据
     */
    public static void sendJsonBy200(HttpServletResponse response, String json) {
        if (!response.isCommitted()) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=utf-8");
            try {
                response.getWriter().print(json);
            } catch (IOException e) {
                throw new InternalAuthenticationServiceException("返回数据写入响应流失败", e);
            }
        }
    }
}
