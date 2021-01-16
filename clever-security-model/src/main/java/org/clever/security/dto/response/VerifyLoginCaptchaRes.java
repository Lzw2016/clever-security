package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.response.BaseResponse;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 16:37 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class VerifyLoginCaptchaRes extends BaseResponse {
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 消息
     */
    private String message;
}
