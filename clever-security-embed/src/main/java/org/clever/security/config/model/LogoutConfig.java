package org.clever.security.config.model;

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
     * 登出成功 - 是否需要跳转
     */
    private Boolean logoutSuccessNeedRedirect = false;

    /**
     * 登出成功默认跳转的页面
     */
    private String logoutSuccessRedirectPage = "/index.html";
}
