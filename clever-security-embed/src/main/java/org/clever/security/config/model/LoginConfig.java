package org.clever.security.config.model;

import lombok.Data;

/**
 * 用户登录配置
 * 作者： lzw<br/>
 * 创建时间：2019-04-25 19:01 <br/>
 */
@Data
public class LoginConfig {

    /**
     * 登录页面
     */
    private String loginPage = "/index.html";

    /**
     * 登录请求URL
     */
    private String loginUrl = "/login";

    /**
     * 登录只支持POST请求
     */
    private Boolean postOnly = true;

    /**
     * Json 数据提交方式
     */
    private Boolean jsonDataSubmit = true;

    /**
     * 登录成功 - 是否需要跳转(重定向)
     */
    private Boolean loginSuccessNeedRedirect = false;

    /**
     * 登录成功默认跳转的页面
     */
    private String loginSuccessRedirectPage = "/home.html";

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
     * 登录是否需要验证码
     */
    private Boolean needCaptcha = true;

    /**
     * 登录失败多少次才需要验证码(小于等于0,总是需要验证码)
     */
    private Integer needCaptchaByLoginFailCount = 3;

    /**
     * 验证码有效时间(毫秒)
     */
    private Long captchaEffectiveTime = 60000000L;

    /**
     * 同一个用户并发登录次数限制(-1表示不限制)
     */
    private Integer concurrentLoginCount = 1;

    /**
     * 同一个用户并发登录次数达到最大值之后，是否不允许之后的登录(false 之后登录的把之前登录的挤下来)
     */
    private Boolean notAllowAfterLogin = false;
}
