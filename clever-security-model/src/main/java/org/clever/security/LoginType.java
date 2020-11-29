package org.clever.security;

import lombok.Getter;
import lombok.ToString;

/**
 * 登录方式
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 15:24 <br/>
 */
@ToString
@Getter
public enum LoginType {
    /**
     * 用户名密码
     */
    LoginName_Password(1, "LoginName-Password"),
    /**
     * 手机号验证码
     */
    Telephone_ValidateCode(2, "Telephone-ValidateCode"),
    /**
     * 邮箱验证码
     */
    Email_ValidateCode(3, "Email-ValidateCode"),
    /**
     * “记住我”token
     */
    RememberMe(4, "RememberMe"),
    /**
     * 微信小程序
     */
    WechatSmallProgram(5, "WechatSmallProgram"),
    /**
     * 扫码登录
     */
    ScanCode(6, "ScanCode"),
    ;

    /**
     * 登录方式，1:用户名密码，2:手机号验证码，3:邮箱验证码，4:“记住我”token，5:微信小程序，6:扫码登录
     */
    private final int id;
    /**
     * 登录方式，1:用户名密码，2:手机号验证码，3:邮箱验证码，4:“记住我”token，5:微信小程序，6:扫码登录
     */
    private final String name;

    LoginType(int id, String name) {
        this.id = id;
        this.name = name;
    }

//    public static LoginType valueOf(String name) {
//        if(Objects.equals(LoginType.LoginName_Password.getName(), name) || ) {
//            return LoginType.LoginName_Password;
//        }
//        return null;
//    }
}
