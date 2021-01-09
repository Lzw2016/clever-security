package org.clever.security.embed.config.internal;

import lombok.Data;

import java.io.Serializable;
import java.time.Duration;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 21:45 <br/>
 */
@Data
public class LoginNameRegisterConfig implements Serializable {
    /**
     * 是否启用
     */
    private boolean enable = true;
    /**
     * 是否需要验证码
     */
    private boolean needCaptcha = true;
    /**
     * 图片验证码路径(用于注册时验证)
     */
    private String registerCaptchaPath = "/register/captcha";
    /**
     * 验证码有效时间(默认60秒)
     */
    private Duration effectiveTime = Duration.ofSeconds(60);
}
