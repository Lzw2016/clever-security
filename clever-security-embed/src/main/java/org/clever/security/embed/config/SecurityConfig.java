package org.clever.security.embed.config;

import lombok.Data;
import org.clever.security.Constant;
import org.clever.security.embed.config.internal.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-15 14:31 <br/>
 */
@ConfigurationProperties(prefix = Constant.ConfigPrefix)
@Data
public class SecurityConfig implements Serializable {
    /**
     * 启用Spring Security调试功能
     */
    private boolean enableDebug = false;
    /**
     * 域名称(用于数据隔离)
     */
    private String domainName;
    /**
     * API权限是否默认启用，默认true(true 启用；false 不启用)
     */
    private boolean defaultEnableApiAuth = true;
    /**
     * 不需要认证和授权的Path(支持Ant风格的Path)
     */
    private List<String> ignorePaths = new ArrayList<>();
    /**
     * 不需要授权的Path(支持Ant风格的Path)
     */
    private List<String> ignoreAuthPaths = new ArrayList<>();
    /**
     * 隐藏登录用户不存在的异常
     */
    private boolean hideUserNotFoundException = true;
    /**
     * 未登录时是否需要重定向到401页面
     */
    private boolean notLoginNeedRedirect = false;
    /**
     * 未登录时是否需要重定向
     */
    private String notLoginRedirectPage = "/index.html";
    /**
     * 无权访问时是否需要重定向到403页面
     */
    private boolean forbiddenNeedRedirect = false;
    /**
     * 403页面地址(无权访问时的重定向地址)
     */
    private String forbiddenRedirectPage = "/403.html";

    // ----------------------------------------------------------------------------------------

    /**
     * 用户登录相关配置
     */
    @NestedConfigurationProperty
    private final LoginConfig login = new LoginConfig();
    /**
     * 用户登出相关配置
     */
    @NestedConfigurationProperty
    private final LogoutConfig logout = new LogoutConfig();
    /**
     * 用户登录请求参数加密配置 Aes Key
     */
    @NestedConfigurationProperty
    private final AesKeyConfig loginReqAesKey = new AesKeyConfig();
    /**
     * "记住我"相关配置
     */
    @NestedConfigurationProperty
    private final RememberMeConfig rememberMe = new RememberMeConfig();
    /**
     * token配置(只有JWT Token有效)
     */
    @NestedConfigurationProperty
    private final TokenConfig tokenConfig = new TokenConfig();
    /**
     * 服务间免登陆Token访问配置
     */
    @NestedConfigurationProperty
    private ServerApiAccessToken serverApiAccessToken = new ServerApiAccessToken();
}
