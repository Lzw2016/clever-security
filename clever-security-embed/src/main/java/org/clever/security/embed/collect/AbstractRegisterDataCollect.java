package org.clever.security.embed.collect;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.clever.security.RegisterChannel;
import org.clever.security.RegisterType;
import org.clever.security.model.register.AbstractUserRegisterReq;

import javax.servlet.http.HttpServletRequest;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 18:12 <br/>
 */
public abstract class AbstractRegisterDataCollect implements RegisterDataCollect {

    protected void collectBaseDataByParameter(AbstractUserRegisterReq registerData, HttpServletRequest request) {
        if (registerData.getRegisterChannel() == null) {
            String registerChannel = request.getParameter(AbstractUserRegisterReq.RegisterChannel_ParamName);
            if (StringUtils.isNotBlank(registerChannel)) {
                RegisterChannel registerChannelEnum = RegisterChannel.lookup(registerChannel);
                if (registerChannelEnum == null) {
                    registerChannelEnum = RegisterChannel.lookup(NumberUtils.toInt(registerChannel, -1));
                }
                if (registerChannelEnum != null) {
                    registerData.setRegisterChannel(registerChannelEnum.getName());
                }
            }
        }
        if (registerData.getCaptcha() == null) {
            String captcha = request.getParameter(AbstractUserRegisterReq.RegisterCaptcha_ParamName);
            if (StringUtils.isNotBlank(captcha)) {
                registerData.setCaptcha(captcha);
            }
        }
        if (registerData.getCaptchaDigest() == null) {
            String captchaDigest = request.getParameter(AbstractUserRegisterReq.RegisterCaptchaDigest_ParamName);
            if (StringUtils.isNotBlank(captchaDigest)) {
                registerData.setCaptchaDigest(captchaDigest);
            }
        }
    }

    protected RegisterType getRegisterType(HttpServletRequest request) {
        String registerType = request.getParameter(AbstractUserRegisterReq.RegisterType_ParamName);
        RegisterType registerTypeEnum = RegisterType.lookup(registerType);
        if (registerTypeEnum == null) {
            registerTypeEnum = RegisterType.lookup(NumberUtils.toInt(registerType, -1));
        }
        return registerTypeEnum;
    }
}
