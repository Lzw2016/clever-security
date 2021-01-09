package org.clever.security.embed.config.internal;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登出配置
 * 作者： lzw<br/>
 * 创建时间：2019-04-25 19:02 <br/>
 */
@Data
public class LogoutConfig implements Serializable {
    /**
     * 登出请求路径
     */
    private String logoutPath = "/logout";
    /**
     * 登出后 - 是否需要重定向
     */
    private boolean logoutNeedRedirect = false;
    /**
     * 登出后跳转地址
     */
    private String logoutRedirectPage = "/index.html";
}
