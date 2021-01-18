package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.response.BaseResponse;

import java.util.Date;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/01/18 15:04 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class GetBindSmsCaptchaRes extends BaseResponse {
    /**
     * 验证码
     */
    private String code;
    /**
     * 验证码签名
     */
    private String digest;
    /**
     * 图片验证码内容(Base64编码处理)
     */
    private String codeContent;
    /**
     * 验证码过期时间
     */
    private Date expiredTime;
}
