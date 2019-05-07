package org.clever.security.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;
import org.clever.security.entity.Permission;

import java.util.Date;
import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-03 9:35 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleInfoRes extends BaseResponse {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("角色说明")
    private String description;

    @ApiModelProperty("创建时间")
    private Date createAt;

    @ApiModelProperty("更新时间")
    private Date updateAt;

    @ApiModelProperty("拥有权限")
    private List<Permission> permissionList;
}
