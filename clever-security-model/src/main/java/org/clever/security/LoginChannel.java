package org.clever.security;

import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Objects;

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

    public static LoginChannel lookup(Object obj) {
        String name = String.valueOf(obj);
        LoginChannel loginChannel = lookup(name);
        if (loginChannel == null) {
            loginChannel = lookup(NumberUtils.toInt(name, -1));
        }
        return loginChannel;
    }

    protected static LoginChannel lookup(String name) {
        if (PC_Admin.getName().equalsIgnoreCase(name)) {
            return LoginChannel.PC_Admin;
        } else if (PC_Web.getName().equalsIgnoreCase(name)) {
            return LoginChannel.PC_Web;
        } else if (H5.getName().equalsIgnoreCase(name)) {
            return LoginChannel.H5;
        } else if (IOS_APP.getName().equalsIgnoreCase(name)) {
            return LoginChannel.IOS_APP;
        } else if (Android_APP.getName().equalsIgnoreCase(name)) {
            return LoginChannel.Android_APP;
        } else if (WechatSmallProgram.getName().equalsIgnoreCase(name)) {
            return LoginChannel.WechatSmallProgram;
        } else {
            return null;
        }
    }

    protected static LoginChannel lookup(int id) {
        if (Objects.equals(PC_Admin.getId(), id)) {
            return LoginChannel.PC_Admin;
        } else if (Objects.equals(PC_Web.getId(), id)) {
            return LoginChannel.PC_Web;
        } else if (Objects.equals(H5.getId(), id)) {
            return LoginChannel.H5;
        } else if (Objects.equals(IOS_APP.getId(), id)) {
            return LoginChannel.IOS_APP;
        } else if (Objects.equals(Android_APP.getId(), id)) {
            return LoginChannel.Android_APP;
        } else if (Objects.equals(WechatSmallProgram.getId(), id)) {
            return LoginChannel.WechatSmallProgram;
        } else {
            return null;
        }
    }
}
