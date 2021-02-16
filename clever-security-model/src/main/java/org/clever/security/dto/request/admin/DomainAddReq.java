package org.clever.security.dto.request.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/16 17:05 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DomainAddReq extends BaseRequest {
    /**
     * 域名称
     */
    @NotBlank(message = "域名称不能为空")
    private String name;

    /**
     * Redis前缀
     */
    @NotBlank(message = "Redis前缀不能为空")
    private String redisNameSpace;

    /**
     * 说明
     */
    private String description;
}
