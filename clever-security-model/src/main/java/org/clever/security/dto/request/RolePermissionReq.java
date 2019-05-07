package org.clever.security.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-04 13:30 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RolePermissionReq extends BaseRequest {

    @ApiModelProperty("角色名")
    @NotBlank
    private String roleName;

    @ApiModelProperty("角色字符串")
    @NotBlank
    private String permissionStr;
}
