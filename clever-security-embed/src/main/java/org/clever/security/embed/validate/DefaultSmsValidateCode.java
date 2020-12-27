package org.clever.security.embed.validate;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.third.validate.SmsValidateCode;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/27 16:55 <br/>
 */
@Slf4j
public class DefaultSmsValidateCode implements SmsValidateCode {
    @Override
    public void sendSms(int type, String telephone, String code) {
        // TODO 发送短信验证码
        log.info("### 发送短信验证码-> | telephone={} | code={}", telephone, code);
    }
}
