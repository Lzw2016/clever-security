package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.response.BaseResponse;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/01/18 15:13 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class VerifyBindSmsValidateCodeRes extends BaseResponse {
    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 消息
     */
    private String message;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 验证码是否已过期
     */
    private boolean expired;
}
