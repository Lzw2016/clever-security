package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 17:52 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class DomainExistsUserReq extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;
    /**
     * 用户id
     */
    @NotBlank(message = "用户id不能为空")
    private String uid;

    public DomainExistsUserReq(Long domainId) {
        this.domainId = domainId;
    }
}
