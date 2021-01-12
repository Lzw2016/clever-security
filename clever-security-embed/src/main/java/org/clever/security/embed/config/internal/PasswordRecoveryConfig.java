package org.clever.security.embed.config.internal;

import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.io.Serializable;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/12 20:53 <br/>
 */
@Data
public class PasswordRecoveryConfig implements Serializable {
    /**
     * 密码找回请求地址
     */
    private String registerPath = "/password_recovery";
    /**
     * 使用短信找回密码
     */
    @NestedConfigurationProperty
    private final PasswordSmsRecoveryConfig passwordSmsRecovery = new PasswordSmsRecoveryConfig();
    /**
     * 使用邮箱找回密码
     */
    @NestedConfigurationProperty
    private final PasswordEmailRecoveryConfig passwordEmailRecovery = new PasswordEmailRecoveryConfig();
}
