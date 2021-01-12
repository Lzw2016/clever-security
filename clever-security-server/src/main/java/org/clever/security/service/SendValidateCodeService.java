package org.clever.security.service;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.imgvalidate.ImageValidateCageUtils;
import org.clever.security.third.validate.SendEmailValidateCode;
import org.clever.security.third.validate.SendSmsValidateCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-10 18:13 <br/>
 */
@Component
@Slf4j
public class SendValidateCodeService {
    protected static final ThreadPoolExecutor Executor_Service = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors() * 8,
            Runtime.getRuntime().availableProcessors() * 8,
            60L,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>()
    );
    @Autowired
    private SendSmsValidateCode sendSmsValidateCode;
    @Autowired
    private SendEmailValidateCode sendEmailValidateCode;

    /**
     * 发送短信验证码
     */
    public void sendSms(int type, String telephone, String code) {
        Executor_Service.execute(() -> {
            try {
                sendSmsValidateCode.sendSms(type, telephone, code);
            } catch (Exception e) {
                log.error("登录短信验证码发送失败", e);
            }
        });
    }

    /**
     * 发送邮件验证码
     */
    public void sendEmail(int type, String email, String code) {
        Executor_Service.execute(() -> {
            try {
                byte[] image = ImageValidateCageUtils.createImage(code);
                sendEmailValidateCode.sendEmail(type, email, code, image);
            } catch (Exception e) {
                log.error("登录邮件验证码发送失败", e);
            }
        });
    }
}
