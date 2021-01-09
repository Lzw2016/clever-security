package org.clever.security.embed.register;

import org.clever.security.client.RegisterSupportClient;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.exception.RegisterException;
import org.clever.security.model.register.AbstractUserRegisterReq;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 19:08 <br/>
 */
public class DefaultVerifyRegisterData implements VerifyRegisterData {
    private final RegisterSupportClient registerSupportClient;

    public DefaultVerifyRegisterData(RegisterSupportClient registerSupportClient) {
        Assert.notNull(registerSupportClient, "参数registerSupportClient不能为null");
        this.registerSupportClient = registerSupportClient;
    }

    @Override
    public boolean isSupported(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserRegisterReq registerReq) {
        return true;
    }

    @Override
    public void verify(SecurityConfig securityConfig, HttpServletRequest request, AbstractUserRegisterReq registerReq) throws RegisterException {

    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
