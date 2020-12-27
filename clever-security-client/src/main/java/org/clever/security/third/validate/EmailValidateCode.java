package org.clever.security.third.validate;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/27 16:50 <br/>
 */
public interface EmailValidateCode {
    /**
     * 发送邮件验证码
     *
     * @param type  验证码类型，1:登录验证码，2:找回密码验证码，3:重置密码(修改密码)验证码
     * @param email 邮箱地址
     * @param code  验证码内容
     * @param image 图片验证码
     */
    void sendEmail(int type, String email, String code, byte[] image);
}
