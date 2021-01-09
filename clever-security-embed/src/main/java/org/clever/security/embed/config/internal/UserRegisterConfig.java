package org.clever.security.embed.config.internal;

import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.io.Serializable;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 12:57 <br/>
 */
@Data
public class UserRegisterConfig implements Serializable {
    /**
     * 用户注册路径
     */
    private String registerPath = "/user_register";
    /**
     * 登录名注册配置
     */
    @NestedConfigurationProperty
    private LoginNameRegisterConfig loginNameRegister = new LoginNameRegisterConfig();
    /**
     * 短信验证码注册配置
     */
    @NestedConfigurationProperty
    private SmsRegisterConfig smsRegister = new SmsRegisterConfig();
    /**
     * 邮箱注册配置
     */
    @NestedConfigurationProperty
    private EmailRegisterConfig emailRegister = new EmailRegisterConfig();

    // 其他三方平台账号登录注册(oauth2.0)
}

