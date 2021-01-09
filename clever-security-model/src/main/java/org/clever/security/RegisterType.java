package org.clever.security;

import lombok.Getter;
import lombok.ToString;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 14:19 <br/>
 */
@ToString
@Getter
public enum RegisterType {
    /**
     * 登录名密码注册
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
     * 注册类型ID，1:登录名密码注册，2:手机号注册，3:邮箱注册，4:微信小程序注册，
     */
    private final int id;
    /**
     * 注册类型，1:登录名密码注册，2:手机号注册，3:邮箱注册，4:微信小程序注册，
     */
    private final String name;

    RegisterType(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
