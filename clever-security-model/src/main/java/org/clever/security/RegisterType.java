package org.clever.security;

import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 14:19 <br/>
 */
@ToString
@Getter
public enum RegisterType {
    /**
     * 登录名注册
     */
    LoginName_Password(1, "LoginName-Password"),
    /**
     * 手机号注册
     */
    Sms_ValidateCode(2, "Sms-ValidateCode"),
    /**
     * 邮箱注册
     */
    Email_ValidateCode(3, "Email-ValidateCode"),
    /**
     * 微信小程序注册
     */
    WechatSmallProgram(4, "WechatSmallProgram"),
    ;
    /**
     * 注册类型ID，1:登录名注册，2:手机号注册，3:邮箱注册，4:微信小程序注册，
     */
    private final int id;
    /**
     * 注册类型，1:登录名注册，2:手机号注册，3:邮箱注册，4:微信小程序注册，
     */
    private final String name;

    RegisterType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static RegisterType lookup(Object obj) {
        String name = String.valueOf(obj);
        RegisterType registerType = lookup(name);
        if (registerType == null) {
            registerType = lookup(NumberUtils.toInt(name, -1));
        }
        return registerType;
    }

    protected static RegisterType lookup(String name) {
        if (LoginName_Password.getName().equalsIgnoreCase(name)) {
            return RegisterType.LoginName_Password;
        } else if (Sms_ValidateCode.getName().equalsIgnoreCase(name)) {
            return RegisterType.Sms_ValidateCode;
        } else if (Email_ValidateCode.getName().equalsIgnoreCase(name)) {
            return RegisterType.Email_ValidateCode;
        } else if (WechatSmallProgram.getName().equalsIgnoreCase(name)) {
            return RegisterType.WechatSmallProgram;
        } else {
            return null;
        }
    }

    protected static RegisterType lookup(int id) {
        if (Objects.equals(LoginName_Password.getId(), id)) {
            return RegisterType.LoginName_Password;
        } else if (Objects.equals(Sms_ValidateCode.getId(), id)) {
            return RegisterType.Sms_ValidateCode;
        } else if (Objects.equals(Email_ValidateCode.getId(), id)) {
            return RegisterType.Email_ValidateCode;
        } else if (Objects.equals(WechatSmallProgram.getId(), id)) {
            return RegisterType.WechatSmallProgram;
        } else {
            return null;
        }
    }
}
