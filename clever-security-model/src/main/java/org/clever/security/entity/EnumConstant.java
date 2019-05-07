package org.clever.security.entity;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-17 14:15 <br/>
 */
public class EnumConstant {

    /**
     * 帐号是否锁定，0：未锁定
     */
    public static final Integer User_Locked_0 = 0;

    /**
     * 帐号是否锁定，1：锁定
     */
    public static final Integer User_Locked_1 = 1;

    /**
     * 是否启用，0：禁用
     */
    public static final Integer User_Enabled_0 = 0;

    /**
     * 是否启用，1：启用
     */
    public static final Integer User_Enabled_1 = 1;

    /**
     * 需要授权才允许访问（1：需要）
     */
    public static final Integer Permission_NeedAuthorization_1 = 1;

    /**
     * 需要授权才允许访问（2：不需要）
     */
    public static final Integer Permission_NeedAuthorization_2 = 2;

    /**
     * 权限类型，1:web资源权限
     */
    public static final Integer Permission_ResourcesType_1 = 1;

    /**
     * 权限类型，2:菜单权限
     */
    public static final Integer Permission_ResourcesType_2 = 2;

    /**
     * 权限类型，3:ui权限
     */
    public static final Integer Permission_ResourcesType_3 = 3;

    /**
     * controller路由资源是否存在，0：不存在
     */
    public static final Integer WebPermission_targetExist_0 = 0;

    /**
     * controller路由资源是否存在，1：存在
     */
    public static final Integer WebPermission_targetExist_1 = 1;

    /**
     * 登录状态，0：未知
     */
    public static final Integer UserLoginLog_LoginState_0 = 0;

    /**
     * 登录状态，1：已登录
     */
    public static final Integer UserLoginLog_LoginState_1 = 1;

    /**
     * 登录状态，2：登录已过期
     */
    public static final Integer UserLoginLog_LoginState_2 = 2;

    /**
     * 登录类型，0：session-cookie
     */
    public static final Integer ServiceSys_LoginModel_0 = 0;

    /**
     * 登录类型，1：jwt-token
     */
    public static final Integer ServiceSys_LoginModel_1 = 1;
}
