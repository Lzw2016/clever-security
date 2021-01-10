package org.clever.security;

import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.math.NumberUtils;

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
    RefreshToken(4, "RefreshToken"),
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
     * 登录方式，1:用户名密码，2:手机号验证码，3:邮箱验证码，4:刷新令牌，5:微信小程序，6:扫码登录
     */
    private final int id;
    /**
     * 登录方式，1:用户名密码，2:手机号验证码，3:邮箱验证码，4:刷新令牌，5:微信小程序，6:扫码登录
     */
    private final String name;

    LoginType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static LoginType lookup(Object obj) {
        String name = String.valueOf(obj);
        LoginType loginType = lookup(name);
        if (loginType == null) {
            loginType = lookup(NumberUtils.toInt(name, -1));
        }
        return loginType;
    }

    protected static LoginType lookup(String name) {
        if (LoginName_Password.getName().equalsIgnoreCase(name)) {
            return LoginType.LoginName_Password;
        } else if (Sms_ValidateCode.getName().equalsIgnoreCase(name)) {
            return LoginType.Sms_ValidateCode;
        } else if (Email_ValidateCode.getName().equalsIgnoreCase(name)) {
            return LoginType.Email_ValidateCode;
        } else if (RefreshToken.getName().equalsIgnoreCase(name)) {
            return LoginType.RefreshToken;
        } else if (WechatSmallProgram.getName().equalsIgnoreCase(name)) {
            return LoginType.WechatSmallProgram;
        } else if (ScanCode.getName().equalsIgnoreCase(name)) {
            return LoginType.ScanCode;
        } else {
            return null;
        }
    }

    protected static LoginType lookup(int id) {
        if (Objects.equals(LoginName_Password.getId(), id)) {
            return LoginType.LoginName_Password;
        } else if (Objects.equals(Sms_ValidateCode.getId(), id)) {
            return LoginType.Sms_ValidateCode;
        } else if (Objects.equals(Email_ValidateCode.getId(), id)) {
            return LoginType.Email_ValidateCode;
        } else if (Objects.equals(RefreshToken.getId(), id)) {
            return LoginType.RefreshToken;
        } else if (Objects.equals(WechatSmallProgram.getId(), id)) {
            return LoginType.WechatSmallProgram;
        } else if (Objects.equals(ScanCode.getId(), id)) {
            return LoginType.ScanCode;
        } else {
            return null;
        }
    }
}
