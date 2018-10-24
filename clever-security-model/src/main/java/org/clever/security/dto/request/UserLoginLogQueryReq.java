package org.clever.security.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import java.io.Serializable;
import java.util.Date;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-21 22:12 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserLoginLogQueryReq extends BaseRequest {

    @ApiModelProperty("用户登录名")
    private String username;

    @ApiModelProperty("系统名称")
    private String sysName;

    @ApiModelProperty("登录时间 - 开始")
    private Date loginTimeStart;

    @ApiModelProperty("登录时间 - 结束")
    private Date loginTimeEnd;

    @ApiModelProperty("登录状态，0：未知；1：已登录；2：登录已过期")
    private Integer loginState;

    @ApiModelProperty("手机号")
    private String telephone;

    @ApiModelProperty("邮箱")
    private String email;
}
