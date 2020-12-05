package org.clever.security.model.login;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.clever.security.LoginType;

import java.util.Objects;

/**
 * 用户登录请求数据
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 14:32 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractUserLoginReq extends BaseRequest {
    public static final String LoginType_ParamName = "loginType";
    public static final String RememberMe_ParamName = "rememberMe";
    public static final String LoginChannel_ParamName = "loginChannel";

    /**
     * “记住我”参数
     */
    private Boolean rememberMe;
    /**
     * 登录渠道，0:PC-Admin，1:PC-Web，2:H5，3:IOS-APP，4:Android-APP，5:微信小程序
     */
    private String loginChannel;

    /**
     * 登录方式，1:用户名密码，2:手机号验证码，3:邮箱验证码，4:“记住我”token，5:微信小程序，6:扫码登录
     */
    abstract public LoginType getLoginType();

    public Boolean getRememberMe() {
        return Objects.equals(rememberMe, Boolean.TRUE);
    }
}
