package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.response.BaseResponse;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 16:37 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class GetLoginFailedCountAndCaptchaRes extends BaseResponse {
    /**
     * 登录失败次数
     */
    private int failedCount;
    /**
     * 最后登录失败时间
     */
    private Date lastLoginTime;

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
