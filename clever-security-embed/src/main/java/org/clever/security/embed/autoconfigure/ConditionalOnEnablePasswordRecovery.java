package org.clever.security.embed.autoconfigure;

import org.clever.security.Constant;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/12 21:22 <br/>
 */
public class ConditionalOnEnablePasswordRecovery extends AnyNestedCondition {
    public ConditionalOnEnablePasswordRecovery() {
        super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "password-recovery.sms-recovery.enable", havingValue = "true", matchIfMissing = true)
    static class EnablePasswordSmsRecovery {
    }

    @ConditionalOnProperty(prefix = Constant.ConfigPrefix, name = "password-recovery.email-recovery.enable", havingValue = "true", matchIfMissing = true)
    static class EnablePasswordEmailRecovery {
    }
}
