package org.clever.security.jwt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-15 14:31 <br/>
 */
@ConfigurationProperties(prefix = "clever.security.jwt")
@Component
@Data
public class SecurityConfig {

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
    private Integer waitSpringContextInitCount = 2;

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
     * 用户登录相关配置
     */
    private final Login login = new Login();

    /**
     * "记住我"相关配置
     */
    private final RememberMe rememberMe = new RememberMe();

    /**
     * 用户登出相关配置
     */
    private final Logout logout = new Logout();

    /**
     * 用户登录请求Aes Key
     */
    private final AesKey loginReqAesKey = new AesKey();

    /**
     * token配置
     */
    private final TokenConfig tokenConfig = new TokenConfig();

    @Data
    public static class Login {
        /**
         * 登录页面
         */
        private String loginPage = "/index.html";

        /**
         * 登录请求URL
         */
        private String loginUrl = "/login";

        /**
         * 登录用户名参数名
         */
        private String usernameParameter = "username";

        /**
         * 登录密码参数名
         */
        private String passwordParameter = "password";

        /**
         * 登录验证码参数名
         */
        private String captchaParameter = "captcha";

        /**
         * 登录只支持POST请求
         */
        private Boolean postOnly = true;

        /**
         * Json 数据提交方式
         */
        private Boolean jsonDataSubmit = true;

        /**
         * 登录失败 - 是否需要跳转(重定向)
         */
        private Boolean loginFailureNeedRedirect = false;

        /**
         * 登录失败 - 是否需要跳转(请求转发) 优先级低于loginFailureNeedRedirect
         */
        private Boolean loginFailureNeedForward = false;

        /**
         * 登录失败默认跳转的页面
         */
        private String loginFailureRedirectPage = "/index.html";

        /**
         * 登录是否需呀验证码
         */
        private Boolean needCaptcha = true;

        /**
         * 登录失败多少次才需呀验证码(小于等于0,总是需呀验证码)
         */
        private Integer needCaptchaByLoginFailCount = 3;

        /**
         * 验证码有效时间(毫秒)
         */
        private Long captchaEffectiveTime = 60000L;

        /**
         * 同一个用户并发登录次数限制(-1表示不限制)
         */
        private Integer concurrentLoginCount = 1;

        /**
         * 同一个用户并发登录次数达到最大值之后，是否不允许之后的登录(false 之后登录的把之前登录的挤下来)
         */
        private Boolean notAllowAfterLogin = false;
    }

    @Data
    public static class Logout {

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

    @Data
    static class RememberMe {
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
        private Integer validitySeconds = 2592000;

        /**
         * "记住我"参数名
         */
        private String rememberMeParameterName = "remember-me";
    }

    @Data
    public static class AesKey {
        /**
         * 密码AES加密 key(Hex编码) -- 请求数据，与前端一致
         */
        private String reqPasswordAesKey;

        /**
         * 密码AES加密 iv(Hex编码) -- 请求数据，与前端一致
         */
        private String reqPasswordAesIv;
    }

    @Data
    public static class TokenConfig {

        /**
         * Token Redis前缀
         */
        private String redisNamespace = "jwt-token";

        /**
         * Token签名密钥
         */
        private String secretKey = "clever-security-jwt";

        /**
         * Token有效时间(默认：7天)
         */
        private Duration tokenValidity = Duration.ofDays(7);

        /**
         * Token记住我有效时间(默认：30天)
         */
        private Duration tokenValidityForRememberMe = Duration.ofDays(30);

        /**
         * 刷新令牌有效时间
         */
        private Duration refreshTokenValidity = Duration.ofDays(30);
    }
}
