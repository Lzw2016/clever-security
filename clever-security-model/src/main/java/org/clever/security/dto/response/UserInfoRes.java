package org.clever.security.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;
import org.clever.security.entity.Permission;
import org.clever.security.entity.Role;

import java.util.Date;
import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-02 22:46 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInfoRes extends BaseResponse {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("登录名(一条记录的手机号不能当另一条记录的用户名用)")
    private String username;

    @ApiModelProperty("用户类型，0：系统内建，1：外部系统用户")
    private Integer userType;

    @ApiModelProperty("手机号")
    private String telephone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("帐号过期时间")
    private Date expiredTime;

    @ApiModelProperty("帐号是否锁定，0：未锁定；1：锁定")
    private Integer locked;

    @ApiModelProperty("是否启用，0：禁用；1：启用")
    private Integer enabled;

    @ApiModelProperty("说明")
    private String description;

    @ApiModelProperty("创建时间")
    private Date createAt;

    @ApiModelProperty("更新时间")
    private Date updateAt;

    @ApiModelProperty("角色信息")
    private List<Role> roleList;

    @ApiModelProperty("权限信息")
    private List<Permission> permissionList;

    @ApiModelProperty("授权系统信息")
    private List<String> sysNameList;

}
