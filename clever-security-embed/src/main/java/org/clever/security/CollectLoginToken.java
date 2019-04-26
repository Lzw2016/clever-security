package org.clever.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 收集用户登录认证信息接口
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2019-04-26 16:36 <br/>
 */
public interface CollectLoginToken {

    /**
     * 是否支持收集当前请求的登录信息
     *
     * @return 支持解析返回true
     */
    boolean supports(HttpServletRequest request);

    /**
     * 收集用户登录认证信息
     *
     * @return 返回Authentication子类
     */
    Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException;
}
