package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 21:34 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SendLoginValidateCodeForSmsReq extends BaseRequest {
    @NotBlank(message = "域id不能为空")
    private Long domainId;
    @NotBlank(message = "手机号不能为空")
    private String telephone;
    /**
     * 验证码过期时间
     */
    private int effectiveTimeMilli = 120 * 1000;
    /**
     * 一天发送短信验证码的最大数(小于等于0表示不限制)
     */
    private int maxSendNumInDay = 32;

    public SendLoginValidateCodeForSmsReq(Long domainId) {
        this.domainId = domainId;
    }
}
