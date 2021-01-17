package org.clever.security.embed.config.internal;

import lombok.Data;

import java.io.Serializable;
import java.time.Duration;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/17 20:55 <br/>
 */
@Data
public class BindEmailConfig implements Serializable {
    /**
     * 是否启用
     */
    private boolean enable = true;
    /**
     * 绑定邮箱请求路径
     */
    private String bindEmailPath = "/bind_email";
    /**
     * 是否需要验证码
     */
    private boolean needCaptcha = true;
    /**
     * 图片验证码路径(用于发送邮箱验证码之前的验证)
     */
    private String captchaPath = "/bind_email/captcha";
    /**
     * 图片验证码有效时间(默认60秒)
     */
    private Duration captchaEffectiveTime = Duration.ofSeconds(60);
    /**
     * 邮箱验证码(请求Path)
     */
    private String emailValidateCodePath = "/bind/email";
    /**
     * 验证码有效时间(默认300秒)
     */
    private Duration effectiveTime = Duration.ofSeconds(300);
    /**
     * 一天发送邮箱验证码的最大数(小于等于0表示不限制)
     */
    private int maxSendNumInDay = 16;

    // 邮箱换绑
}
