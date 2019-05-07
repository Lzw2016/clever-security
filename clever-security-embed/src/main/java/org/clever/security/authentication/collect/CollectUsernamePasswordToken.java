package org.clever.security.authentication.collect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.utils.mapper.JacksonMapper;
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

    @Override
    public boolean supports(HttpServletRequest request, boolean isSubmitBody) throws IOException {
        String loginType;
        String username;
        String password;
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
        } else {
            // 使用Parameter提交数据
            loginType = StringUtils.trimToEmpty(request.getParameter(LOGIN_TYPE_PARAM));
            username = StringUtils.trimToEmpty(request.getParameter(USERNAME_PARAM));
            password = StringUtils.trimToEmpty(request.getParameter(PASSWORD_PARAM));
        }
        if (StringUtils.isNotBlank(loginType)) {
            return LoginTypeConstant.UsernamePassword.equalsIgnoreCase(loginType);
        }
        return StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password);
    }


    @Override
    public BaseLoginToken attemptAuthentication(HttpServletRequest request, boolean isSubmitBody) throws AuthenticationException, IOException {
        UsernamePasswordToken usernamePasswordToken;
        if (isSubmitBody) {
            // 使用Json方式提交数据
            Object json = request.getAttribute(Constant.Login_Data_Body_Request_Key);
            if (json == null) {
                json = IOUtils.toString(request.getReader());
                request.setAttribute(Constant.Login_Data_Body_Request_Key, json);
            }
            usernamePasswordToken = JacksonMapper.nonEmptyMapper().fromJson(json.toString(), UsernamePasswordToken.class);
        } else {
            // 使用Parameter提交数据
            String username = StringUtils.trimToEmpty(request.getParameter(USERNAME_PARAM));
            String password = StringUtils.trimToEmpty(request.getParameter(PASSWORD_PARAM));
            String captcha = StringUtils.trimToEmpty(request.getParameter(CAPTCHA_PARAM));
            String captchaDigest = StringUtils.trimToEmpty(request.getParameter(CAPTCHA_DIGEST_PARAM));
            boolean rememberMe = Boolean.parseBoolean(StringUtils.trimToEmpty(request.getParameter(REMEMBER_ME_PARAM)));
            // 创建Token
            usernamePasswordToken = new UsernamePasswordToken(username, password);
            usernamePasswordToken.setCaptcha(captcha);
            usernamePasswordToken.setCaptchaDigest(captchaDigest);
            usernamePasswordToken.setRememberMe(rememberMe);
        }
        return usernamePasswordToken;
    }
}
