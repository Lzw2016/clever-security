package org.clever.security.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;
import org.clever.security.entity.Permission;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-04 12:56 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleBindPermissionRes extends BaseResponse {

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("权限列表")
    private List<Permission> permissionList;
}
