package org.clever.security.embed.config.internal;

import lombok.Data;

import java.io.Serializable;
import java.time.Duration;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 21:51 <br/>
 */
@Data
public class SmsRegisterConfig implements Serializable {
    /**
     * 是否启用
     */
    private boolean enable = false;
    /**
     * 是否需要验证码
     */
    private boolean needCaptcha = true;
    /**
     * 图片验证码路径(用于发送短信验证码之前的验证)
     */
    private String registerCaptchaPath = "/register/sms/captcha";
    /**
     * 图片验证码有效时间(默认60秒)
     */
    private Duration captchaEffectiveTime = Duration.ofSeconds(60);
    /**
     * 注册手机验证码(请求Path)
     */
    private String registerSmsValidateCodePath = "/register/sms";
    /**
     * 验证码有效时间(默认120秒)
     */
    private Duration effectiveTime = Duration.ofSeconds(120);
    /**
     * 一天发送短信验证码的最大数(小于等于0表示不限制)
     */
    private int maxSendNumInDay = 16;
}
