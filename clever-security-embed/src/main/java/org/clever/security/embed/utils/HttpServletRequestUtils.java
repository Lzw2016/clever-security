package org.clever.security.embed.utils;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 作者：ymx <br/>
 * 创建时间：2020/12/04 20:42 <br/>
 */
public class HttpServletRequestUtils {
    /**
     * 获取request  post请求的body数据
     *
     * @param request 响应对象
     * @return JSONObject     body数据json
     */
    public static JSONObject getBodyData(HttpServletRequest request) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String jsonStr;
            StringBuilder result = new StringBuilder();
            try {
                while ((jsonStr = reader.readLine()) != null) {
                    result.append(jsonStr);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return JSONObject.parseObject(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
