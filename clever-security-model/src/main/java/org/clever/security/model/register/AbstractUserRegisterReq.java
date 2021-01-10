package org.clever.security.model.register;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.clever.security.RegisterType;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/01 20:41 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractUserRegisterReq extends BaseRequest {
    public static final String RegisterType_ParamName = "registerType";
    public static final String RegisterChannel_ParamName = "registerChannel";
    public static final String RegisterCaptcha_ParamName = "captcha";
    public static final String RegisterCaptchaDigest_ParamName = "captchaDigest";

    /**
     * 注册渠道，0:PC-Admin，1:PC-Web，2:H5，3:IOS-APP，4:Android-APP，5:微信小程序
     */
    private String registerChannel;
    /**
     * 注册验证码
     */
    private String captcha;
    /**
     * 注册验证码签名(校验验证码时需要)
     */
    private String captchaDigest;

    /**
     * 注册类型，1:登录名注册，2:手机号注册，3:邮箱注册，4:微信小程序注册，
     */
    public abstract RegisterType getRegisterType();
}
