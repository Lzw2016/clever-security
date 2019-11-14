package org.clever.security.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-03 19:05 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionUpdateReq extends BaseRequest {

    @ApiModelProperty("权限标题")
    @Length(max = 255)
    private String title;

    @ApiModelProperty("唯一权限标识字符串")
    @Length(min = 6, max = 255)
    private String permissionStr;

    @ApiModelProperty("权限类型，1:API接口, 2:菜单权限，3:ui权限，4:自定义")
    @Range(min = 1, max = 4)
    private Integer resourcesType;

    @ApiModelProperty("权限说明")
    @Length(max = 127)
    private String description;

    @ApiModelProperty("需要授权才允许访问（1：需要；2：不需要）")
    @Range(min = 1, max = 2)
    private Integer needAuthorization;
}
