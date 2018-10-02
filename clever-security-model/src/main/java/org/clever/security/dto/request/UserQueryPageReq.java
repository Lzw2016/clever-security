package org.clever.security.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.QueryByPage;

import java.util.Date;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-02 21:22 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryPageReq extends QueryByPage {

    @ApiModelProperty("登录名(一条记录的手机号不能当另一条记录的用户名用)")
    private String username;

    @ApiModelProperty("用户类型，0：系统内建，1：外部系统用户")
    private Integer userType;

    @ApiModelProperty("手机号")
    private String telephone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("帐号过期时间-开始")
    private Date expiredTimeStart;

    @ApiModelProperty("帐号过期时间-结束")
    private Date expiredTimeEnd;

    @ApiModelProperty("帐号是否锁定，0：未锁定；1：锁定")
    private Integer locked;

    @ApiModelProperty("是否启用，0：禁用；1：启用")
    private Integer enabled;
}
