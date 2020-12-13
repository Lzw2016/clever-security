package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 17:34 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetLoginSmsValidateCodeReq extends BaseRequest {
    @NotBlank(message = "域id不能为空")
    private Long domainId;
    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "(?:0|86|\\+86)?1[3456789]\\d{9}", message = "手机号格式错误")
    private String telephone;

    public GetLoginSmsValidateCodeReq(Long domainId) {
        this.domainId = domainId;
    }
}
