package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.response.BaseResponse;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-10 14:36 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class SendSmsValidateCodeRes extends BaseResponse {
    /**
     * 验证码
     */
    private String code;
    /**
     * 验证码签名
     */
    private String digest;
    /**
     * 验证码过期时间
     */
    private Date expiredTime;
}
