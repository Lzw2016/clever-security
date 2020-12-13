package org.clever.security.embed.config.internal;

import lombok.Data;

import java.time.Duration;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 11:26 <br/>
 */
@Data
public class SmsValidateCodeLoginConfig {
    /**
     * 是否启用手机号验证码登录
     */
    private boolean enable = false;
    /**
     * 登录手机验证码(请求Path)
     */
    private String loginSmsValidateCodePath = "/sms_code";
    /**
     * 验证码有效时间(默认120秒)
     */
    private Duration effectiveTime = Duration.ofSeconds(120);
    /**
     * 一天发送短信验证码的最大数(小于等于0表示不限制)
     */
    private int maxSendNumInDay = 32;
}
