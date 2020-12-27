package org.clever.security.third.validate;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/27 16:47 <br/>
 */
public interface SmsValidateCode {
    /**
     * 发送短信验证码
     *
     * @param type      验证码类型，1:登录验证码，2:找回密码验证码，3:重置密码(修改密码)验证码
     * @param telephone 手机号
     * @param code      验证码内容
     */
    void sendSms(int type, String telephone, String code);
}
