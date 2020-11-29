package org.clever.security.embed.collect;

import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.model.login.AbstractUserLoginReq;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:05 <br/>
 */
public class DefaultLoginDataCollect implements ILoginDataCollect {

    @Override
    public boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request) {
        // TODO isSupported
        return false;
    }

    @Override
    public AbstractUserLoginReq collectLoginData(SecurityConfig securityConfig, HttpServletRequest request) {
        // TODO collectLoginData
        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}