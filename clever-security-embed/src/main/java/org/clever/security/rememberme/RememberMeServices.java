package org.clever.security.rememberme;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.Constant;
import org.clever.security.model.LoginUserDetails;
import org.clever.security.token.SecurityContextToken;
import org.clever.security.token.login.RememberMeToken;
import org.json.JSONObject;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;

/**
 * "记住我"功能实现
 * 作者： lzw<br/>
 * 创建时间：2018-09-20 20:23 <br/>
 */
@Slf4j
public class RememberMeServices extends PersistentTokenBasedRememberMeServices {

    public static final String REMEMBER_ME_KEY = "remember-me-key";
    public static final String REMEMBER_ME_COOKIE_NAME = "remember-me";


    public RememberMeServices(
            String key,
            UserDetailsService userDetailsService,
            PersistentTokenRepository tokenRepository,
            UserDetailsChecker userDetailsChecker) {
        super(key, userDetailsService, tokenRepository);
        this.setUserDetailsChecker(userDetailsChecker);
    }

    /**
     * 判断是否需要"记住我"的功能
     */
    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        String rememberMe = request.getParameter(parameter);
        if (rememberMe == null) {
            Object json = request.getAttribute(Constant.Login_Data_Body_Request_Key);
            if (json != null) {
                JSONObject object = new JSONObject(json.toString());
                rememberMe = object.optString(parameter);
            }
        }
        if (rememberMe != null) {
            if (rememberMe.equalsIgnoreCase("true")
                    || rememberMe.equalsIgnoreCase("on")
                    || rememberMe.equalsIgnoreCase("yes")
                    || rememberMe.equals("1")) {
                return true;
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Did not send remember-me cookie (principal did not set " + "parameter '" + parameter + "')");
        }
        return false;
    }

    /**
     * 通过RememberMe登录创建LoginToken而非RememberMeAuthenticationToken，以免遇到403时跳转到登录页面<br/>
     * 不合理应该修改UserLoginEntryPoint.java
     */
    @Override
    protected Authentication createSuccessfulAuthentication(HttpServletRequest request, UserDetails user) {
        AbstractAuthenticationToken authentication;
        if (user instanceof LoginUserDetails) {
            LoginUserDetails loginUserDetails = (LoginUserDetails) user;
            RememberMeToken rememberMeToken = new RememberMeToken();
            rememberMeToken.setUsername(loginUserDetails.getUsername());
            authentication = new SecurityContextToken(rememberMeToken, loginUserDetails);
        } else {
            authentication = new RememberMeAuthenticationToken(
                    this.getKey(),
                    user,
                    user.getAuthorities());
        }
        // 设置用户 "details" 属性(设置请求IP和SessionID) -- 需要提前创建Session
        request.getSession();
        authentication.setDetails(this.getAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }
}
