package org.clever.security;

import lombok.Getter;
import lombok.ToString;

/**
 * 登录渠道
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 15:11 <br/>
 */
@ToString
@Getter
public enum LoginChannel {
    /**
     * PC-Admin
     */
    PC_Admin(0, "PC-Admin"),
    /**
     * PC-Web
     */
    PC_Web(1, "PC-Web"),
    /**
     * H5
     */
    H5(2, "H5"),
    /**
     * IOS-APP
     */
    IOS_APP(3, "IOS-APP"),
    /**
     * Android-APP
     */
    Android_APP(4, "Android-APP"),
    /**
     * 微信小程序
     */
    WechatSmallProgram(5, "WechatSmallProgram"),
    ;

    /**
     * 登录渠道ID，0:PC-Admin，1:PC-Web，2:H5，3:IOS-APP，4:Android-APP，5:微信小程序
     */
    private final int id;
    /**
     * 登录渠道名称，0:PC-Admin，1:PC-Web，2:H5，3:IOS-APP，4:Android-APP，5:微信小程序
     */
    private final String name;

    LoginChannel(int id, String name) {
        this.id = id;
        this.name = name;
    }

//    public static LoginType valueOf(String name) {
//
//    }
}
