package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/27 13:36 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class GetDomainReq extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;

    public GetDomainReq(Long domainId) {
        this.domainId = domainId;
    }
}
