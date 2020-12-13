package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 17:36 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetLoginEmailValidateCodeReq extends BaseRequest {
    @NotBlank(message = "域id不能为空")
    private Long domainId;
    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    private String email;

    public GetLoginEmailValidateCodeReq(Long domainId) {
        this.domainId = domainId;
    }
}
