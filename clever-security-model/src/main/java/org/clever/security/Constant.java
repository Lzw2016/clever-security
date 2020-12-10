package org.clever.security;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-04-25 19:57 <br/>
 */
public interface Constant {
    /**
     * 扫码登录，扫码
     */
    int ScanCodeLogin_Action_Scan = 1;
    /**
     * 扫码登录，确认登录
     */
    int ScanCodeLogin_Action_Confirm = 2;

    /**
     * 配置文件前缀
     */
    String ConfigPrefix = "clever.security";

//    /**
//     * 登录请求数据 Key
//     */
//    public static final String Login_Data_Body_Request_Key = "Login_Data_Body_Request_Key";
//
//    /**
//     * 请求登录用户名 Key
//     */
//    public static final String Login_Username_Request_Key = "Login_Username_Request_Key";
//
//    /**
//     * 登录验证码 Key
//     */
//    public static final String Login_Captcha_Session_Key = "Login_Captcha_Session_Key";
//
//    /**
//     * 登录失败次数 Key
//     */
//    public static final String Login_Fail_Count_Session_Key = "Login_Fail_Count_Session_Key";
}
