package org.clever.security.session.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-17 21:38 <br/>
 */
public class HttpRequestUtils {

    /**
     * 判断是否要返回Json
     */
    public static boolean isJsonResponse(HttpServletRequest request) {
        String getFlag = request.getMethod();
        String jsonFlag = StringUtils.trimToEmpty(request.getHeader("Accept"));
        String ajaxFlag = request.getHeader("X-Requested-With");
        return !getFlag.equalsIgnoreCase("GET") || jsonFlag.contains("application/json") || Objects.equals("XMLHttpRequest", ajaxFlag);
    }

    /**
     * 判断是否要返回Json (只用用于判断登录请求)
     */
    public static boolean isJsonResponseByLogin(HttpServletRequest request) {
        String jsonFlag = StringUtils.trimToEmpty(request.getHeader("Accept"));
        String ajaxFlag = request.getHeader("X-Requested-With");
        return jsonFlag.contains("application/json") || Objects.equals("XMLHttpRequest", ajaxFlag);
    }
}
