package org.clever.security.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-04 13:26 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserRoleReq extends BaseRequest {

    @ApiModelProperty("用户名")
    @NotBlank
    private String username;

    @ApiModelProperty("角色名")
    @NotBlank
    private String roleName;
}
