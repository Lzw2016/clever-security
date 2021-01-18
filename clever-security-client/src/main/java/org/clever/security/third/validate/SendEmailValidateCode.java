package org.clever.security.third.validate;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/27 16:50 <br/>
 */
public interface SendEmailValidateCode {
    /**
     * 发送邮件验证码
     *
     * @param type  验证码类型，1:登录验证码，2:找回密码验证码，3:修改密码验证码，4:登录名注册验证码，5:短信注册图片验证码，6:短信注册短信验证码，7:邮箱注册图片验证码，8:邮箱注册邮箱验证码
     * @param email 邮箱地址
     * @param code  验证码内容
     * @param image 图片验证码
     */
    void sendEmail(int type, String email, String code, byte[] image);
}
