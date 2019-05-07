package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-18 15:19 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AccessDeniedRes extends BaseResponse {

    /**
     * 错误消息
     */
    private String message = "没有访问权限";

    /**
     * 时间戳
     */
    private Long timestamp = System.currentTimeMillis();

    public AccessDeniedRes() {
    }

    public AccessDeniedRes(String message) {
        this.message = message;
    }
}
