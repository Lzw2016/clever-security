package org.clever.security.model.login;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;
import org.clever.security.model.UserInfo;
import org.springframework.util.Assert;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/02 20:58 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginRes extends BaseResponse {
    /**
     * 是否登录成功
     */
    private final boolean success;
    /**
     * 登录成功用户信息
     */
    private final UserInfo userInfo;
    /**
     * JWT-Token
     */
    private final String jwtToken;
    /**
     * 刷新Token
     */
    private final String refreshToken;
    /**
     * 登录返回消息
     */
    private final String message;

    private LoginRes(boolean success, UserInfo userInfo, String jwtToken, String refreshToken, String message) {
        this.success = success;
        this.userInfo = userInfo;
        this.jwtToken = jwtToken;
        this.refreshToken = refreshToken;
        this.message = message;
    }

    public static LoginRes loginSuccess(UserInfo userInfo, String jwtToken, String refreshToken) {
        Assert.notNull(userInfo, "参数userInfo不能为null");
        Assert.hasText(jwtToken, "参数jwtToken不能为空");
        return new LoginRes(true, userInfo, jwtToken, refreshToken, "登录成功");
    }

    public static LoginRes loginFailure(String message) {
        return new LoginRes(false, null, null, null, message);
    }
}
