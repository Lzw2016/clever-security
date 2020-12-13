package org.clever.security;

import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

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
    Sms_ValidateCode(2, "Sms-ValidateCode"),
    /**
     * 邮箱验证码
     */
    Email_ValidateCode(3, "Email-ValidateCode"),
    /**
     * 刷新令牌
     */
    RefreshToken(4, "RememberMe"),
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

    public static LoginType lookup(String name) {
        if (LoginName_Password.getName().equalsIgnoreCase(name)) {
            return LoginName_Password;
        } else if (Sms_ValidateCode.getName().equalsIgnoreCase(name)) {
            return Sms_ValidateCode;
        } else if (Email_ValidateCode.getName().equalsIgnoreCase(name)) {
            return Email_ValidateCode;
        } else if (RefreshToken.getName().equalsIgnoreCase(name)) {
            return RefreshToken;
        } else if (WechatSmallProgram.getName().equalsIgnoreCase(name)) {
            return WechatSmallProgram;
        } else if (ScanCode.getName().equalsIgnoreCase(name)) {
            return ScanCode;
        } else {
            return null;
        }
    }

    public static LoginType lookup(int id) {
        if (Objects.equals(LoginName_Password.getId(), id)) {
            return LoginName_Password;
        } else if (Objects.equals(Sms_ValidateCode.getId(), id)) {
            return Sms_ValidateCode;
        } else if (Objects.equals(Email_ValidateCode.getId(), id)) {
            return Email_ValidateCode;
        } else if (Objects.equals(RefreshToken.getId(), id)) {
            return RefreshToken;
        } else if (Objects.equals(WechatSmallProgram.getId(), id)) {
            return WechatSmallProgram;
        } else if (Objects.equals(ScanCode.getId(), id)) {
            return ScanCode;
        } else {
            return null;
        }
    }
}
