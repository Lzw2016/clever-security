package org.clever.security.embed.config.internal;

import lombok.Data;

import java.io.Serializable;
import java.time.Duration;

/**
 * 用户登录配置
 * 作者： lzw<br/>
 * 创建时间：2019-04-25 19:01 <br/>
 */
@Data
public class LoginConfig implements Serializable {
    /**
     * 登录页面
     */
    private String loginPage = "/index.html";
    /**
     * 登录请求Path
     */
    private String loginPath = "/login";

    // TODO 手机验证码、邮箱验证码、图片验证码、扫码登录相关配置

    /**
     * 登录只支持POST请求
     */
    private boolean postOnly = true;
    /**
     * 登录成功 - 是否需要重定向到指定页面
     */
    private boolean loginSuccessNeedRedirect = false;
    /**
     * 登录成功重定向的地址
     */
    private String loginSuccessRedirectPage = "/home.html";
    /**
     * 登录失败 - 是否需要重定向
     */
    private boolean loginFailureNeedRedirect = false;
    /**
     * 登录失败跳转地址
     */
    private String loginFailurePage = "/index.html";
    /**
     * 登录是否需要验证码
     */
    private boolean needCaptcha = true;
    /**
     * 登录失败多少次才需要验证码(小于等于0表示总是需要验证码)
     */
    private int needCaptchaByLoginFailCount = 3;
    /**
     * 验证码有效时间(默认60秒)
     */
    private Duration captchaEffectiveTime = Duration.ofSeconds(60);
    /**
     * 同一个用户并发登录次数限制(小于等于0表示不限制)
     */
    private int concurrentLoginCount = 1;
    /**
     * 同一个用户并发登录次数达到最大值之后,是否后登陆的挤下前登录的(false表示登录次数达到最大值后,之后的用户无法登录)
     */
    private boolean allowAfterLogin = true;
}
