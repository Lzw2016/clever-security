package org.clever.security.embed.autoconfigure;

import org.clever.security.Constant;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 17:43 <br/>
 */
public class ConditionalOnUserRegisterFilter extends AnyNestedCondition {
    public ConditionalOnUserRegisterFilter() {
        super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "register.enable-login-name-register", havingValue = "true")
    static class EnableLoginNameRegister {

    }

    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "register.enable-sms-register", havingValue = "false")
    static class EnableSmsRegister {
    }

    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "register.enable-email-register", havingValue = "false")
    static class EnableEmailRegister {

    }
}
