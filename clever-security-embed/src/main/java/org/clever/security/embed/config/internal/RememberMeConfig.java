package org.clever.security.embed.config.internal;

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
    private boolean enable = true;
    /**
     * 总是"记住我"
     */
    private boolean alwaysRemember = false;
    /**
     * "记住我"有效时间(默认30天)
     */
    private Duration validity = Duration.ofDays(30);
}
