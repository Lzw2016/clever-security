package org.clever.security.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.hibernate.validator.constraints.Length;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-03 9:30 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleUpdateReq extends BaseRequest {

    @ApiModelProperty("角色名称")
    @Length(max = 63)
    private String name;

    @ApiModelProperty("角色说明")
    @Length(max = 1023)
    private String description;
}
