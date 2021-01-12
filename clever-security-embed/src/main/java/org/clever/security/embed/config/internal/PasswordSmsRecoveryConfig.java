package org.clever.security.embed.config.internal;

import lombok.Data;

import java.io.Serializable;
import java.time.Duration;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/12 21:00 <br/>
 */
@Data
public class PasswordSmsRecoveryConfig implements Serializable {
    /**
     * 是否启用
     */
    private boolean enable = true;
    /**
     * 手机验证码(请求Path)
     */
    private String smsValidateCodePath = "/password_recovery/sms";
    /**
     * 验证码有效时间(默认120秒)
     */
    private Duration effectiveTime = Duration.ofSeconds(120);
    /**
     * 一天发送短信验证码的最大数(小于等于0表示不限制)
     */
    private int maxSendNumInDay = 8;
}
