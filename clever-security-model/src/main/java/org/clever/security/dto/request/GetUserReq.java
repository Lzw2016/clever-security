package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 18:00 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetUserReq extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;
    /**
     * 用户id
     */
    @NotBlank(message = "用户id不能为空")
    private String uid;

    public GetUserReq(Long domainId) {
        this.domainId = domainId;
    }
}
