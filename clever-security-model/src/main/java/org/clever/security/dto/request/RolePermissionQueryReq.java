package org.clever.security.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.QueryByPage;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-03 12:55 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RolePermissionQueryReq extends QueryByPage {

    @ApiModelProperty("系统(或服务)名称")
    private String sysName;

    @ApiModelProperty("资源标题(模糊匹配)")
    private String title;

    @ApiModelProperty("资源访问所需要的权限标识字符串(模糊匹配)")
    private String permissionStr;

    @ApiModelProperty("资源类型（1:请求URL地址, 2:其他资源）")
    private Integer resourcesType;

    @ApiModelProperty("Spring Controller类名称(模糊匹配)")
    private String targetClass;

    @ApiModelProperty("Spring Controller类的方法名称")
    private String targetMethod;

    @ApiModelProperty("资源URL地址(模糊匹配)")
    private String resourcesUrl;

    @ApiModelProperty("需要授权才允许访问（1：需要；2：不需要）")
    private Integer needAuthorization;

    @ApiModelProperty("Spring Controller路由资源是否存在，0：不存在；1：存在")
    private Integer targetExist;
}
