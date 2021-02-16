package org.clever.security.dto.request.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.clever.common.validation.StringNotBlank;

import javax.validation.constraints.NotNull;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/16 18:29 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DomainUpdateReq extends BaseRequest {
    /**
     * 域id
     */
    @NotNull(message = "域id不能为null")
    private Long id;

    /**
     * 域名称
     */
    @StringNotBlank(message = "域名称不能为空")
    private String name;

    /**
     * Redis前缀
     */
    @StringNotBlank(message = "Redis前缀不能为空")
    private String redisNameSpace;

    /**
     * 说明
     */
    private String description;
}
