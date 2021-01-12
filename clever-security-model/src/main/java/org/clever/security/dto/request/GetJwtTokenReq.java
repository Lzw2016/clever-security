package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * 作者：lizw <br/>
 * 创建时间：2020-12-16 20:46 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class GetJwtTokenReq extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;
    /**
     * JWT-Token id(系统自动生成且不会变化)
     */
    @NotNull(message = "JWT-Token id不能为null")
    private Long id;

    public GetJwtTokenReq(Long domainId) {
        this.domainId = domainId;
    }
}
