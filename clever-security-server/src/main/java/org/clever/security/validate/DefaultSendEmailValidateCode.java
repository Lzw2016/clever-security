package org.clever.security.validate;//package org.clever.security.embed.validate;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.third.validate.SendEmailValidateCode;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/27 16:55 <br/>
 */
@Slf4j
public class DefaultSendEmailValidateCode implements SendEmailValidateCode {
    @Override
    public void sendEmail(int type, String email, String code, byte[] image) {
        // TODO 发送邮件验证码
        log.info("### 发送邮件验证码-> | type={} | email={} | code={}", type, email, code);
    }
}
