package org.clever.security.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.third.validate.SendEmailValidateCode;
import org.clever.security.third.validate.SendSmsValidateCode;
import org.clever.security.validate.DefaultSendEmailValidateCode;
import org.clever.security.validate.DefaultSendSmsValidateCode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-10 15:12 <br/>
 */
@Order
@Configuration
@Slf4j
public class ApplicationBeans {

    @Bean("sendEmailValidateCode")
    @ConditionalOnMissingBean(name = "sendEmailValidateCode")
    public SendEmailValidateCode sendEmailValidateCode() {
        return new DefaultSendEmailValidateCode();
    }

    @Bean("sendSmsValidateCode")
    @ConditionalOnMissingBean(name = "sendSmsValidateCode")
    public SendSmsValidateCode sendSmsValidateCode() {
        return new DefaultSendSmsValidateCode();
    }
}
