package org.clever.security.entity;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/10 20:29 <br/>
 */
public interface EnumConstant {
    /**
     * API接口是否存在，0：不存在
     */
    int ApiPermission_ApiExist_0 = 0;
    /**
     * API接口是否存在，1：存在
     */
    int ApiPermission_ApiExist_1 = 1;

    /**
     * JWT-Token是否禁用，0:未禁用
     */
    int JwtToken_Disable_0 = 0;
    /**
     * JWT-Token是否禁用，1:已禁用
     */
    int JwtToken_Disable_1 = 1;
    /**
     * 刷新Token状态，0:无效(已使用)
     */
    int JwtToken_RefreshTokenState_0 = 0;
    /**
     * 刷新Token状态，1:有效(未使用)
     */
    int JwtToken_RefreshTokenState_1 = 1;

    /**
     * 菜单隐藏模式，0：不隐藏(显示)
     */
    int MenuPermission_Hide_0 = 0;
    /**
     * 菜单隐藏模式，1：隐藏
     */
    int MenuPermission_Hide_1 = 1;

    /**
     * 权限类型，1:API权限
     */
    int Permission_ResourcesType_1 = 1;
    /**
     * 权限类型，2:菜单权限
     */
    int Permission_ResourcesType_2 = 2;
    /**
     * 权限类型，3:UI组件权限
     */
    int Permission_ResourcesType_3 = 3;
    /**
     * 是否启用授权，0:不启用
     */
    int Permission_Enabled_0 = 0;
    /**
     * 是否启用授权，1:启用
     */
    int Permission_Enabled_1 = 1;

    /**
     * 扫描二维码状态，0:已创建(待扫描)
     */
    int ScanCodeLogin_ScanCodeState_0 = 0;
    /**
     * 扫描二维码状态，1:已扫描(待确认)
     */
    int ScanCodeLogin_ScanCodeState_1 = 1;
    /**
     * 扫描二维码状态，2:已确认(待登录)
     */
    int ScanCodeLogin_ScanCodeState_2 = 2;
    /**
     * 扫描二维码状态，3:登录成功
     */
    int ScanCodeLogin_ScanCodeState_3 = 3;
    /**
     * 扫描二维码状态，4:已失效
     */
    int ScanCodeLogin_ScanCodeState_4 = 4;

    /**
     * 是否启用，0:禁用
     */
    int User_Enabled_0 = 0;
    /**
     * 是否启用，1:启用
     */
    int User_Enabled_1 = 1;
    /**
     * 用户注册渠道，0:管理员
     */
    int User_RegisterChannel_0 = 0;
    /**
     * 用户注册渠道，1:PC-Web
     */
    int User_RegisterChannel_1 = 1;
    /**
     * 用户注册渠道，2:H5
     */
    int User_RegisterChannel_2 = 2;
    /**
     * 用户注册渠道，3:IOS-APP
     */
    int User_RegisterChannel_3 = 3;
    /**
     * 用户注册渠道，4:Android-APP
     */
    int User_RegisterChannel_4 = 4;
    /**
     * 用户注册渠道，5:微信小程序
     */
    int User_RegisterChannel_5 = 5;
    /**
     * 用户来源，0:系统注册
     */
    int User_FromSource_0 = 0;
    /**
     * 用户来源，1:外部导入(同步)
     */
    int User_FromSource_1 = 1;

    /**
     * 登录渠道，0:PC-Admin
     */
    int UserLoginLog_LoginChannel_0 = 0;
    /**
     * 登录渠道，1:PC-Web
     */
    int UserLoginLog_LoginChannel_1 = 1;
    /**
     * 登录渠道，2:H5
     */
    int UserLoginLog_LoginChannel_2 = 2;
    /**
     * 登录渠道，3:IOS-APP
     */
    int UserLoginLog_LoginChannel_3 = 3;
    /**
     * 登录渠道，4:Android-APP
     */
    int UserLoginLog_LoginChannel_4 = 4;
    /**
     * 登录渠道，5:微信小程序
     */
    int UserLoginLog_LoginChannel_5 = 5;
    /**
     * 登录方式，1:用户名密码
     */
    int UserLoginLog_LoginType_1 = 1;
    /**
     * 登录方式，2:手机号验证码
     */
    int UserLoginLog_LoginType_2 = 2;
    /**
     * 登录方式，3:邮箱验证码
     */
    int UserLoginLog_LoginType_3 = 3;
    /**
     * 登录方式，4:刷新token
     */
    int UserLoginLog_LoginType_4 = 4;
    /**
     * 登录方式，5:微信小程序
     */
    int UserLoginLog_LoginType_5 = 5;
    /**
     * 登录方式，6:扫码登录
     */
    int UserLoginLog_LoginType_6 = 6;
    /**
     * 登录状态，0:登录失败
     */
    int UserLoginLog_LoginState_0 = 0;
    /**
     * 登录状态，1:登录成功
     */
    int UserLoginLog_LoginState_1 = 1;

    /**
     * 验证码类型，1:登录验证码
     */
    int ValidateCode_Type_1 = 1;
    /**
     * 验证码类型，2:找回密码验证码
     */
    int ValidateCode_Type_2 = 2;
    /**
     * 验证码类型，3:更新密码验证码
     */
    int ValidateCode_Type_3 = 3;
    /**
     * 验证码类型，4:登录名注册图片验证码
     */
    int ValidateCode_Type_4 = 4;
    /**
     * 验证码类型，5:短信注册图片验证码
     */
    int ValidateCode_Type_5 = 5;
    /**
     * 验证码类型，6:短信注册短信验证码
     */
    int ValidateCode_Type_6 = 6;
    /**
     * 验证码类型，7:邮箱注册图片验证码
     */
    int ValidateCode_Type_7 = 7;
    /**
     * 验证码类型，8:邮箱注册邮箱验证码
     */
    int ValidateCode_Type_8 = 8;
    /**
     * 验证码类型，9:手机换绑图片验证码
     */
    int ValidateCode_Type_9 = 9;
    /**
     * 验证码类型，10:手机换绑短信验证码
     */
    int ValidateCode_Type_10 = 10;
    /**
     * 验证码类型，11:邮箱换绑图片验证码
     */
    int ValidateCode_Type_11 = 11;
    /**
     * 验证码类型，12:邮箱换绑邮箱验证码
     */
    int ValidateCode_Type_12 = 12;
    /**
     * 验证码发送渠道，0:不需要发送
     */
    int ValidateCode_SendChannel_0 = 0;
    /**
     * 验证码发送渠道，1:短信
     */
    int ValidateCode_SendChannel_1 = 1;
    /**
     * 验证码发送渠道，2:email
     */
    int ValidateCode_SendChannel_2 = 2;

    /**
     * 数据删除标志，0:未删除
     */
    int LoginFailedCount_DeleteFlag_0 = 0;
    /**
     * 数据删除标志，1:已删除
     */
    int LoginFailedCount_DeleteFlag_1 = 1;

    /**
     * 注册类型，1:登录名注册
     */
    int UserRegisterLog_RegisterType_1 = 1;
    /**
     * 注册类型，2:手机号注册
     */
    int UserRegisterLog_RegisterType_2 = 2;
    /**
     * 注册类型，3:邮箱注册
     */
    int UserRegisterLog_RegisterType_3 = 3;
    /**
     * 注册类型，4:微信小程序注册，
     */
    int UserRegisterLog_RegisterType_4 = 4;
    /**
     * 注册结果，0:注册失败
     */
    int UserRegisterLog_RequestResult_1 = 1;
    /**
     * 注册结果，1:注册成功且创建用户
     */
    int UserRegisterLog_RequestResult_2 = 2;
    /**
     * 注册结果，2:注册成功仅关联到域
     */
    int UserRegisterLog_RequestResult_3 = 3;
}
