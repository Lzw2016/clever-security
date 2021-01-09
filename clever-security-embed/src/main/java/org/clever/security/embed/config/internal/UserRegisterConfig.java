package org.clever.security.embed.config.internal;

import lombok.Data;

import java.io.Serializable;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 12:57 <br/>
 */
@Data
public class UserRegisterConfig implements Serializable {
    /**
     * 是否启用邮箱验证码登录
     */
    private boolean enable = false;
    /**
     * 用户注册路径
     */
    private String registerPath = "/user_register";
    /**
     * 是否启用验证码
     */
    private boolean enableCaptcha = true;


    // 使用手机号验证码注册
    // 使用邮箱验证码注册
    // 其他三方平台账号登录注册(oauth2.0)
}
