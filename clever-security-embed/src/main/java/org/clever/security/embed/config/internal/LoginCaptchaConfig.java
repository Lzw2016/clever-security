package org.clever.security.embed.config.internal;

import lombok.Data;

import java.io.Serializable;
import java.time.Duration;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 11:10 <br/>
 */
@Data
public class LoginCaptchaConfig implements Serializable {
    /**
     * 登录是否需要验证码
     */
    private boolean needCaptcha = true;
    /**
     * 登录图片验证码(请求Path)
     */
    private String loginCaptchaPath = "/login/captcha";
    /**
     * 登录失败多少次才需要验证码(小于等于0表示总是需要验证码)
     */
    private int needCaptchaByLoginFailedCount = 3;
    /**
     * 验证码有效时间(默认60秒)
     */
    private Duration effectiveTime = Duration.ofSeconds(60);
}
