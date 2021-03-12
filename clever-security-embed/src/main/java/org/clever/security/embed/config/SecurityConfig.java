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
     * 域名称(用于数据隔离)
     */
    private Long domainId;
    /**
     * 是否需要初始化权限数据
     */
    private boolean initAuth = false;
    /**
     * API权限是否默认启用，默认true(true 启用；false 不启用)
     */
    private boolean defaultEnableApiAuth = true;
    /**
     * 不需要认证和授权的Path(支持Ant风格的Path)
     */
    private final List<String> ignorePaths = new ArrayList<>();
    /**
     * 不需要授权的Path(支持Ant风格的Path)
     */
    private final List<String> ignoreAuthPaths = new ArrayList<>();
    /**
     * 不需要授权的ControllerClass
     */
    private final List<String> ignoreAuthClass = new ArrayList<>();
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
    /**
     * 获取当前用户信息请求地址
     */
    private String currentUserPath = "/current_user";

    // ----------------------------------------------------------------------------------------
    /**
     * 用户请求参数加密配置AesKey(登录、注册等敏感接口使用)
     */
    @NestedConfigurationProperty
    private final AesKeyConfig reqAesKey = new AesKeyConfig();
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
     * 用户注册配置
     */
    @NestedConfigurationProperty
    private final UserRegisterConfig register = new UserRegisterConfig();
    /**
     * token配置(JWT-Token有效)
     */
    @NestedConfigurationProperty
    private final TokenConfig tokenConfig = new TokenConfig();
    /**
     * 密码找回配置
     */
    @NestedConfigurationProperty
    private final PasswordRecoveryConfig passwordRecovery = new PasswordRecoveryConfig();
    /**
     * 手机号绑定配置
     */
    @NestedConfigurationProperty
    private final BindTelephoneConfig bindTelephone = new BindTelephoneConfig();
    /**
     * 邮箱绑定配置
     */
    @NestedConfigurationProperty
    private final BindEmailConfig bindEmail = new BindEmailConfig();
    /**
     * 修改密码配置
     */
    @NestedConfigurationProperty
    private final UpdatePasswordConfig updatePassword = new UpdatePasswordConfig();
    /**
     * 服务间免登陆Token访问配置
     */
    @NestedConfigurationProperty
    private final ServerAccessConfig serverAccess = new ServerAccessConfig();
}
