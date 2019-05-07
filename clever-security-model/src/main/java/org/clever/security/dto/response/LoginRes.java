package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-17 13:57 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginRes extends BaseResponse {
    /**
     * 是否登录成功
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
     * 当前登录用户信息
     */
    private UserRes user;

    public LoginRes(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public LoginRes(Boolean success, String message, UserRes user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }
}
