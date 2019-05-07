package org.clever.security.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-03 8:16 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleAddReq extends BaseRequest {

    @ApiModelProperty("角色名称")
    @NotBlank
    @Length(max = 63)
    private String name;

    @ApiModelProperty("角色说明")
    @Length(max = 1023)
    private String description;
}
