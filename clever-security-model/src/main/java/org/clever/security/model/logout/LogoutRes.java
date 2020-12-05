package org.clever.security.model.logout;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;
import org.clever.security.model.UserInfo;
import org.springframework.util.Assert;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/05 16:35 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LogoutRes extends BaseResponse {
    /**
     * 是否登出成功
     */
    private final boolean success;
    /**
     * 登出成功用户信息
     */
    private final UserInfo userInfo;
    /**
     * 登出返回消息
     */
    private final String message;

    private LogoutRes(boolean success, UserInfo userInfo, String message) {
        this.success = success;
        this.userInfo = userInfo;
        this.message = message;
    }

    public static LogoutRes logoutSuccess(UserInfo userInfo) {
        Assert.notNull(userInfo, "参数userInfo不能为null");
        return new LogoutRes(true, userInfo, "登出成功");
    }

    public static LogoutRes logoutFailure(String message) {
        return new LogoutRes(false, null, message);
    }
}
