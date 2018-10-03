package org.clever.security.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-03 16:29 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionAddReq extends BaseRequest {

    @ApiModelProperty("系统(或服务)名称")
    @NotBlank
    @Length(max = 127)
    private String sysName;

    @ApiModelProperty("权限标题")
    @NotBlank
    @Length(max = 255)
    private String title;

    @ApiModelProperty("唯一权限标识字符串")
    @NotBlank
    @Length(max = 255)
    private String permissionStr;

    @ApiModelProperty("权限类型，1:web资源权限, 2:菜单权限，3:ui权限")
    @NotNull
    @Range(min = 2, max = 3)
    private Integer resourcesType;

    @ApiModelProperty("权限说明")
    @Length(max = 127)
    private String description;
}
