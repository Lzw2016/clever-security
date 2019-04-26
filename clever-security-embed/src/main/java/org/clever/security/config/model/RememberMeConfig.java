package org.clever.security.config.model;

import lombok.Data;

import java.time.Duration;

/**
 * 记住我功能配置
 * 作者： lzw<br/>
 * 创建时间：2019-04-25 19:26 <br/>
 */
@Data
public class RememberMeConfig {
    /**
     * 启用"记住我"功能
     */
    private Boolean enable = true;

    /**
     * 总是"记住我"
     */
    private Boolean alwaysRemember = false;

    /**
     * "记住我"有效时间(单位秒，默认一个月)
     */
    private Duration validity = Duration.ofDays(30);

    /**
     * "记住我"参数名
     */
    private String rememberMeParameterName = "remember-me";
}
