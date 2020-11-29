package org.clever.security.embed.config.internal;

import lombok.Data;

/**
 * 用户登出配置
 * 作者： lzw<br/>
 * 创建时间：2019-04-25 19:02 <br/>
 */
@Data
public class LogoutConfig {
    /**
     * 登出请求URL
     */
    private String logoutUrl = "/logout";
    /**
     * 登出成功 - 是否需要重定向
     */
    private boolean logoutSuccessNeedRedirect = false;
    /**
     * 登出成功跳转地址
     */
    private String logoutSuccessRedirectPage = "/index.html";
}
