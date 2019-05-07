package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-21 10:22 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LogoutRes extends BaseResponse {
    /**
     * 是否登出成功
     */
    private Boolean success;

    /**
     * 错误消息
     */
    private String message;

    /**
     * 时间戳
     */
    private Long timestamp = System.currentTimeMillis();

    /**
     * 当前登出用户信息
     */
    private UserRes user;

    public LogoutRes(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public LogoutRes(Boolean success, String message, UserRes user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }
}
