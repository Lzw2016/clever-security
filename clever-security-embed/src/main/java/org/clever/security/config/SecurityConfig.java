package org.clever.security.config;

import lombok.Data;
import org.clever.security.Constant;
import org.clever.security.LoginModel;
import org.clever.security.config.model.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-15 14:31 <br/>
 */
@ConfigurationProperties(prefix = Constant.ConfigPrefix)
@Component
@Data
public class SecurityConfig {

    /**
     * 登入模式
     */
    private LoginModel loginModel = LoginModel.jwt;

    /**
     * 启用Spring Security调试功能
     */
    private Boolean enableDebug = false;
    /**
     * 系统名称
     */
    private String sysName;
    /**
     * 等待spring容器初始化次数(用于系统启动之后初始化系统权限)
     */
    private Integer waitSpringContextInitCount = 0;
    /**
     * 是否需要控制Web资源权限，默认true(true 需要；false 不要)
     */
    private Boolean defaultNeedAuthorization = true;
    /**
     * 不需要认证的URL
     */
    private List<String> ignoreUrls = new ArrayList<>();
    /**
     * 不需要授权的URL
     */
    private List<String> ignoreAuthorizationUrls = new ArrayList<>();
    /**
     * 隐藏登录用户找不到的异常
     */
    private Boolean hideUserNotFoundExceptions = true;
    /**
     * 未登录时是否需要跳转
     */
    private Boolean notLoginNeedForward = false;
    /**
     * 无权访问时是否需要跳转
     */
    private Boolean forbiddenNeedForward = false;
    /**
     * 无权访问时跳转页面(请求转发)
     */
    private String forbiddenForwardPage = "/403.html";

    /**
     * Session 过期需要跳转的地址，值为空就不跳转
     */
    private String sessionExpiredRedirectUrl = "/index.html";

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
