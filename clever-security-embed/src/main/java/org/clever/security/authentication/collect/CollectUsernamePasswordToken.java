package org.clever.security.authentication.collect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.clever.security.Constant;
import org.clever.security.LoginTypeConstant;
import org.clever.security.authentication.CollectLoginToken;
import org.clever.security.token.login.BaseLoginToken;
import org.clever.security.token.login.UsernamePasswordToken;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 收集用户信息 UsernamePasswordToken
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2019-04-28 15:59 <br/>
 */
@Component
@Slf4j
public class CollectUsernamePasswordToken implements CollectLoginToken {

    private static final String USERNAME_PARAM = "username";
    private static final String PASSWORD_PARAM = "password";

    private UsernamePasswordToken readUsernamePasswordToken(HttpServletRequest request, boolean isSubmitBody) throws IOException {
        String loginType;
        String username;
        String password;
        String captcha;
        String captchaDigest;
        boolean rememberMe;
        if (isSubmitBody) {
            // 使用Json方式提交数据
            Object json = request.getAttribute(Constant.Login_Data_Body_Request_Key);
            if (json == null) {
                json = IOUtils.toString(request.getReader());
                request.setAttribute(Constant.Login_Data_Body_Request_Key, json);
            }
            JSONObject object = new JSONObject(json.toString());
            loginType = StringUtils.trimToEmpty(object.optString(LOGIN_TYPE_PARAM));
            username = StringUtils.trimToEmpty(object.optString(USERNAME_PARAM));
            password = StringUtils.trimToEmpty(object.optString(PASSWORD_PARAM));
            captcha = StringUtils.trimToEmpty(object.optString(CAPTCHA_PARAM));
            captchaDigest = StringUtils.trimToEmpty(object.optString(CAPTCHA_DIGEST_PARAM));
            rememberMe = Boolean.parseBoolean(StringUtils.trimToEmpty(object.optString(REMEMBER_ME_PARAM)));
        } else {
            // 使用Parameter提交数据
            loginType = StringUtils.trimToEmpty(request.getParameter(LOGIN_TYPE_PARAM));
            username = StringUtils.trimToEmpty(request.getParameter(USERNAME_PARAM));
            password = StringUtils.trimToEmpty(request.getParameter(PASSWORD_PARAM));
            captcha = StringUtils.trimToEmpty(request.getParameter(CAPTCHA_PARAM));
            captchaDigest = StringUtils.trimToEmpty(request.getParameter(CAPTCHA_DIGEST_PARAM));
            rememberMe = Boolean.parseBoolean(StringUtils.trimToEmpty(request.getParameter(REMEMBER_ME_PARAM)));
        }
        // 创建Token
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        usernamePasswordToken.setLoginType(loginType);
        usernamePasswordToken.setCaptcha(captcha);
        usernamePasswordToken.setCaptchaDigest(captchaDigest);
        usernamePasswordToken.setRememberMe(rememberMe);
        return usernamePasswordToken;
    }

    @Override
    public boolean supports(HttpServletRequest request, boolean isSubmitBody) throws IOException {
        UsernamePasswordToken usernamePasswordToken = readUsernamePasswordToken(request, isSubmitBody);
        if (StringUtils.isNotBlank(usernamePasswordToken.getLoginType())) {
            return LoginTypeConstant.UsernamePassword.equalsIgnoreCase(usernamePasswordToken.getLoginType());
        }
        return StringUtils.isNotBlank(usernamePasswordToken.getUsername()) && StringUtils.isNotBlank(usernamePasswordToken.getPassword());
    }


    @Override
    public BaseLoginToken attemptAuthentication(HttpServletRequest request, boolean isSubmitBody) throws AuthenticationException, IOException {
        return readUsernamePasswordToken(request, isSubmitBody);
    }
}
