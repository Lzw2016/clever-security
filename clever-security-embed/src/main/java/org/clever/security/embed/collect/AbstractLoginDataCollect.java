package org.clever.security.embed.collect;

import org.apache.commons.lang3.StringUtils;
import org.clever.security.LoginChannel;
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
                if (loginChannelEnum != null) {
                    loginData.setLoginChannel(loginChannelEnum.getName());
                }
            }
        }
        if (loginData.getCaptcha() == null) {
            String captcha = request.getParameter(AbstractUserLoginReq.LoginCaptcha_ParamName);
            if (StringUtils.isNotBlank(captcha)) {
                loginData.setCaptcha(captcha);
            }
        }
    }
}
