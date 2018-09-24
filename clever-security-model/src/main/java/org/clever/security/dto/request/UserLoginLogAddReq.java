package org.clever.security.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-24 19:57 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserLoginLogAddReq extends BaseRequest {

    @NotBlank
    @Length(max = 63)
    @ApiModelProperty("用户登录名")
    private String username;

    @NotNull
    @ApiModelProperty("登录时间")
    private Date loginTime;

    @NotBlank
    @Length(max = 63)
    @ApiModelProperty("登录IP")
    private String loginIp;

    @NotBlank
    @ApiModelProperty("登录的用户信息")
    private String authenticationInfo;

    @NotBlank
    @Length(max = 63)
    @ApiModelProperty("登录SessionID")
    private String sessionId;

    @NotNull
    @Range(min = 0, max = 2)
    @ApiModelProperty("登录状态，0：未知；1：已登录；2：登录已过期")
    private Integer loginState;
}
