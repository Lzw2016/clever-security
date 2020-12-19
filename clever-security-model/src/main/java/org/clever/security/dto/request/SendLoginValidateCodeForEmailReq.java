package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 21:27 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SendLoginValidateCodeForEmailReq extends BaseRequest {
    @NotBlank(message = "域id不能为空")
    private Long domainId;
    @NotBlank(message = "email不能为空")
    private String email;
    /**
     * 验证码过期时间
     */
    private int effectiveTimeMilli = 300 * 1000;
    /**
     * 一天发送邮箱验证码的最大数(小于等于0表示不限制)
     */
    private int maxSendNumInDay = 64;

    public SendLoginValidateCodeForEmailReq(Long domainId) {
        this.domainId = domainId;
    }
}
