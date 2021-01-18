package org.clever.security.embed.config.internal;

import lombok.Data;

import java.io.Serializable;
import java.time.Duration;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/18 20:21 <br/>
 */
@Data
public class ResetPasswordConfig implements Serializable {
    /**
     * 是否启用
     */
    private boolean enable = true;
    /**
     * 是否需要验证码
     */
    private boolean needCaptcha = true;
    /**
     * 图片验证码路径(用于发送邮箱/短信验证码之前的验证)
     */
    private String captchaPath = "/set_password/captcha";
    /**
     * 图片验证码有效时间(默认60秒)
     */
    private Duration captchaEffectiveTime = Duration.ofSeconds(60);
    /**
     * 手机验证码(请求Path)
     */
    private String smsValidateCodePath = "/set_password/sms";
    /**
     * 短信验证码有效时间(默认120秒)
     */
    private Duration smsEffectiveTime = Duration.ofSeconds(120);
    /**
     * 一天发送短信验证码的最大数(小于等于0表示不限制)
     */
    private int smsMaxSendNumInDay = 8;

    /**
     * 邮箱验证码(请求Path)
     */
    private String emailValidateCodePath = "/set_password/email";
    /**
     * 邮箱验证码有效时间(默认300秒)
     */
    private Duration emailEffectiveTime = Duration.ofSeconds(300);
    /**
     * 一天发送邮箱验证码的最大数(小于等于0表示不限制)
     */
    private int emailMaxSendNumInDay = 16;

    /**
     * 设置密码(请求Path)
     */
    private String setPasswordPath = "/set_password";
    /**
     * 重置密码(请求Path)
     */
    private String resetPasswordPath = "/reset_password";
}
