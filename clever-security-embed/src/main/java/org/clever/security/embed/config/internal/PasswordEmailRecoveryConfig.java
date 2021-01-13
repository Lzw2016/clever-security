package org.clever.security.embed.config.internal;

import lombok.Data;

import java.io.Serializable;
import java.time.Duration;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/12 21:03 <br/>
 */
@Data
public class PasswordEmailRecoveryConfig implements Serializable {
    /**
     * 是否启用
     */
    private boolean enable = true;
    /**
     * 是否需要验证码
     */
    private boolean needCaptcha = true;
    /**
     * 图片验证码路径(用于发送邮件验证码之前的验证)
     */
    private String captchaPath = "/password_recovery/email/captcha";
    /**
     * 图片验证码有效时间(默认60秒)
     */
    private Duration captchaEffectiveTime = Duration.ofSeconds(60);
    /**
     * 邮箱验证码(请求Path)
     */
    private String emailValidateCodePath = "/password_recovery/email";
    /**
     * 验证码有效时间(默认300秒)
     */
    private Duration effectiveTime = Duration.ofSeconds(300);
    /**
     * 一天发送邮箱验证码的最大数(小于等于0表示不限制)
     */
    private int maxSendNumInDay = 16;
}
