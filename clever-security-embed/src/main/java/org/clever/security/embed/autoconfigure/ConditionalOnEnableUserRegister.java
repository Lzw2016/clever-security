package org.clever.security.embed.autoconfigure;

import org.clever.security.Constant;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 17:43 <br/>
 */
public class ConditionalOnEnableUserRegister extends AnyNestedCondition {
    public ConditionalOnEnableUserRegister() {
        super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "register.login-name-register.enable", havingValue = "true", matchIfMissing = true)
    static class EnableLoginNameRegister {
    }

    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "register.sms-register.enable", havingValue = "true")
    static class EnableSmsRegister {
    }

    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "register.email-register.enable", havingValue = "true")
    static class EnableEmailRegister {
    }
}
