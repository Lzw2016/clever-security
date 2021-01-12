package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/14 21:06 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class DisableFirstJwtTokenReq extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;
    /**
     * 用户id
     */
    @NotBlank(message = "用户id不能为空")
    private String uid;

    /**
     * JWT-Token禁用原因
     */
    private String disableReason;
    /**
     * 禁用Token数量
     */
    @Min(value = 1, message = "禁用Token数量必须大于等于1")
    @NotNull(message = "禁用Token数量不能为null")
    private Integer disableCount;

    public DisableFirstJwtTokenReq(Long domainId) {
        this.domainId = domainId;
    }
}
