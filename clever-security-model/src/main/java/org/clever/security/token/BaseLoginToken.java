package org.clever.security.token;

import org.springframework.security.core.CredentialsContainer;

import java.io.Serializable;

/**
 * 用户登录请求参数基础Token
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2019-04-27 20:46 <br/>
 */
public abstract class BaseLoginToken implements CredentialsContainer, Serializable {

    /**
     * 是否使用记住我功能
     */
    private boolean isRememberMe = false;

    /**
     * 当前登录类型
     */
    private String loginType;
}
