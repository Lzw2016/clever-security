package org.clever.security.entity;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/10 20:29 <br/>
 */
public interface EnumConstant {
    /**
     * API接口是否存在，0：不存在
     */
    Integer ApiPermission_ApiExist_0 = 0;
    /**
     * API接口是否存在，1：存在
     */
    Integer ApiPermission_ApiExist_1 = 1;

    /**
     * JWT-Token是否禁用，0:未禁用
     */
    Integer JwtToken_Disable_0 = 0;
    /**
     * JWT-Token是否禁用，1:已禁用
     */
    Integer JwtToken_Disable_1 = 1;
    /**
     * 刷新Token状态，0:无效(已使用)
     */
    Integer JwtToken_RefreshTokenState_0 = 0;
    /**
     * 刷新Token状态，1:有效(未使用)
     */
    Integer JwtToken_RefreshTokenState_1 = 1;

    /**
     * 菜单隐藏模式，0：不隐藏
     */
    Integer MenuPermission_HideMode_0 = 0;
    /**
     * 菜单隐藏模式，1：隐藏当前菜单和子菜单
     */
    Integer MenuPermission_HideMode_1 = 1;
    /**
     * 菜单隐藏模式，2:隐藏子菜单
     */
    Integer MenuPermission_HideMode_2 = 2;

    /**
     * 权限类型，1:API权限
     */
    Integer Permission_ResourcesType_1 = 1;
    /**
     * 权限类型，2:菜单权限
     */
    Integer Permission_ResourcesType_2 = 2;
    /**
     * 权限类型，3:UI组件权限
     */
    Integer Permission_ResourcesType_3 = 3;
    /**
     * 是否启用授权，0:不启用
     */
    Integer Permission_EnableAuth_0 = 0;
    /**
     * 是否启用授权，1:启用
     */
    Integer Permission_EnableAuth_1 = 1;

    /**
     * 扫描二维码状态，0:已创建(待扫描)
     */
    Integer ScanCodeLogin_ScanCodeState_0 = 0;
    /**
     * 扫描二维码状态，1:已扫描(待确认)
     */
    Integer ScanCodeLogin_ScanCodeState_1 = 1;
    /**
     * 扫描二维码状态，2:已确认(待登录)
     */
    Integer ScanCodeLogin_ScanCodeState_2 = 2;
    /**
     * 扫描二维码状态，3:登录成功
     */
    Integer ScanCodeLogin_ScanCodeState_3 = 3;
    /**
     * 扫描二维码状态，4:已失效
     */
    Integer ScanCodeLogin_ScanCodeState_4 = 4;

    /**
     * 是否启用，0:禁用
     */
    Integer User_Enabled_0 = 0;
    /**
     * 是否启用，1:启用
     */
    Integer User_Enabled_1 = 1;
    /**
     * 用户注册渠道，0:管理员
     */
    Integer User_RegisterChannel_0 = 0;
    /**
     * 用户注册渠道，1:PC-Web
     */
    Integer User_RegisterChannel_1 = 1;
    /**
     * 用户注册渠道，2:H5
     */
    Integer User_RegisterChannel_2 = 2;
    /**
     * 用户注册渠道，3:IOS-APP
     */
    Integer User_RegisterChannel_3 = 3;
    /**
     * 用户注册渠道，4:Android-APP
     */
    Integer User_RegisterChannel_4 = 4;
    /**
     * 用户注册渠道，5:微信小程序
     */
    Integer User_RegisterChannel_5 = 5;
    /**
     * 用户来源，0:系统注册
     */
    Integer User_FromSource_0 = 0;
    /**
     * 用户来源，1:外部导入(同步)
     */
    Integer User_FromSource_1 = 1;

    /**
     * 登录渠道，0:PC-Admin
     */
    Integer UserLoginLog_LoginChannel_0 = 0;
    /**
     * 登录渠道，1:PC-Web
     */
    Integer UserLoginLog_LoginChannel_1 = 1;
    /**
     * 登录渠道，2:H5
     */
    Integer UserLoginLog_LoginChannel_2 = 2;
    /**
     * 登录渠道，3:IOS-APP
     */
    Integer UserLoginLog_LoginChannel_3 = 3;
    /**
     * 登录渠道，4:Android-APP
     */
    Integer UserLoginLog_LoginChannel_4 = 4;
    /**
     * 登录渠道，5:微信小程序
     */
    Integer UserLoginLog_LoginChannel_5 = 5;
    /**
     * 登录方式，1:用户名密码
     */
    Integer UserLoginLog_LoginType_1 = 1;
    /**
     * 登录方式，2:手机号验证码
     */
    Integer UserLoginLog_LoginType_2 = 2;
    /**
     * 登录方式，3:邮箱验证码
     */
    Integer UserLoginLog_LoginType_3 = 3;
    /**
     * 登录方式，4:刷新token
     */
    Integer UserLoginLog_LoginType_4 = 4;
    /**
     * 登录方式，5:微信小程序
     */
    Integer UserLoginLog_LoginType_5 = 5;
    /**
     * 登录方式，6:扫码登录
     */
    Integer UserLoginLog_LoginType_6 = 6;
    /**
     * 登录状态，0:登录失败
     */
    Integer UserLoginLog_LoginState_0 = 0;
    /**
     * 登录状态，1:登录成功
     */
    Integer UserLoginLog_LoginState_1 = 1;

    /**
     * 验证码类型，1:登录验证码
     */
    Integer ValidateCode_Type_1 = 1;
    /**
     * 验证码类型，2:找回密码验证码
     */
    Integer ValidateCode_Type_2 = 2;
    /**
     * 验证码类型，3:重置密码(修改密码)验证码
     */
    Integer ValidateCode_Type_3 = 3;
}
