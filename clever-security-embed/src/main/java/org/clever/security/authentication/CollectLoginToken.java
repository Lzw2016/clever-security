package org.clever.security.authentication;

import org.clever.security.token.login.BaseLoginToken;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 收集用户登录认证信息接口
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2019-04-26 16:36 <br/>
 */
public interface CollectLoginToken {

    String LOGIN_TYPE_PARAM = "loginType";
    String CAPTCHA_PARAM = "captcha";
    String CAPTCHA_DIGEST_PARAM = "captchaDigest";
    String REMEMBER_ME_PARAM = "rememberMe";

    /**
     * 是否支持收集当前请求的登录信息
     *
     * @param request      请求对象
     * @param isSubmitBody 提交数据是否是JsonBody格式
     * @return 支持解析返回true
     */
    boolean supports(HttpServletRequest request, boolean isSubmitBody) throws IOException;

    /**
     * 收集用户登录认证信息
     *
     * @param request      请求对象
     * @param isSubmitBody 提交数据是否是JsonBody格式
     * @return 返回Authentication子类
     */
    BaseLoginToken attemptAuthentication(HttpServletRequest request, boolean isSubmitBody) throws AuthenticationException, IOException;
}
