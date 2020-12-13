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

    public SendLoginValidateCodeForEmailReq(Long domainId) {
        this.domainId = domainId;
    }
}
