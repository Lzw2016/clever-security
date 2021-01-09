package org.clever.security.embed.collect;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.clever.security.LoginChannel;
import org.clever.security.LoginType;
import org.clever.security.model.login.AbstractUserLoginReq;

import javax.servlet.http.HttpServletRequest;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/07 20:24 <br/>
 */
public abstract class AbstractLoginDataCollect implements LoginDataCollect {

    protected void collectBaseDataByParameter(AbstractUserLoginReq loginData, HttpServletRequest request) {
        if (loginData.getLoginChannel() == null) {
            String loginChannel = request.getParameter(AbstractUserLoginReq.LoginChannel_ParamName);
            if (StringUtils.isNotBlank(loginChannel)) {
                LoginChannel loginChannelEnum = LoginChannel.lookup(loginChannel);
                if (loginChannelEnum == null) {
                    loginChannelEnum = LoginChannel.lookup(NumberUtils.toInt(loginChannel, -1));
                }
                if (loginChannelEnum != null) {
                    loginData.setLoginChannel(loginChannelEnum.getId());
                }
            }
        }
        if (loginData.getCaptcha() == null) {
            String captcha = request.getParameter(AbstractUserLoginReq.LoginCaptcha_ParamName);
            if (StringUtils.isNotBlank(captcha)) {
                loginData.setCaptcha(captcha);
            }
        }
        if (loginData.getCaptchaDigest() == null) {
            String captchaDigest = request.getParameter(AbstractUserLoginReq.LoginCaptchaDigest_ParamName);
            if (StringUtils.isNotBlank(captchaDigest)) {
                loginData.setCaptchaDigest(captchaDigest);
            }
        }
    }

    protected LoginType getLoginType(HttpServletRequest request) {
        String loginType = request.getParameter(AbstractUserLoginReq.LoginType_ParamName);
        LoginType loginTypeEnum = LoginType.lookup(loginType);
        if (loginTypeEnum == null) {
            loginTypeEnum = LoginType.lookup(NumberUtils.toInt(loginType, -1));
        }
        return loginTypeEnum;
    }
}
