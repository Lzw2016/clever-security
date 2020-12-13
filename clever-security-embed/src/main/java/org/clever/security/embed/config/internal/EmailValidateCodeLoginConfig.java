package org.clever.security.embed.config.internal;

import lombok.Data;

import java.time.Duration;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 11:30 <br/>
 */
@Data
public class EmailValidateCodeLoginConfig {
    /**
     * 是否启用邮箱验证码登录
     */
    private boolean enable = false;
    /**
     * 登录邮箱验证码(请求Path)
     */
    private String loginEmailValidateCodePath = "/email_code";
    /**
     * 验证码有效时间(默认300秒)
     */
    private Duration effectiveTime = Duration.ofSeconds(300);
    /**
     * 一天发送邮箱验证码的最大数(小于等于0表示不限制)
     */
    private int maxSendNumInDay = 64;
}
