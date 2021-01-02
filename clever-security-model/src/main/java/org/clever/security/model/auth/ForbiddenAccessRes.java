package org.clever.security.model.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;
import org.clever.security.model.UserInfo;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/06 21:21 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ForbiddenAccessRes extends BaseResponse {
    /**
     * 登出返回消息
     */
    private final String message;

    public ForbiddenAccessRes(String message) {
        this.message = message;
    }
}
