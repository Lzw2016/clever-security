package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;

/**
 * 作者：lizw <br/>
 * 创建时间：2020-12-16 22:10 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CacheContextReq extends BaseRequest {
    @NotBlank(message = "域id不能为空")
    private Long domainId;
    /**
     * 用户id
     */
    @NotBlank(message = "用户id不能为空")
    private String uid;

    public CacheContextReq(Long domainId) {
        this.domainId = domainId;
    }
}
