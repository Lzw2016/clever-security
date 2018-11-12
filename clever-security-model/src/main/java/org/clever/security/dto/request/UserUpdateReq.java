package org.clever.security.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-02 23:27 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserUpdateReq extends BaseRequest {

    @ApiModelProperty("密码")
    @Length(min = 6, max = 127)
    private String password;

    @ApiModelProperty("用户类型，0：系统内建，1：外部系统用户")
    @Range(min = 0, max = 1)
    private Integer userType;

    @ApiModelProperty("手机号")
    @Pattern(regexp = "1[0-9]{10}")
    private String telephone;

    @ApiModelProperty("邮箱")
    @Email
    private String email;

    @ApiModelProperty("帐号过期时间")
    private Date expiredTime;

    @ApiModelProperty("帐号是否锁定，0：未锁定；1：锁定")
    @Range(min = 0, max = 1)
    private Integer locked;

    @ApiModelProperty("是否启用，0：禁用；1：启用")
    @Range(min = 0, max = 1)
    private Integer enabled;

    @ApiModelProperty("说明")
    @Length(max = 511)
    private String description;

    @ApiModelProperty("系统名称列表")
    @Size(max = 100)
    private Set<String> sysNameList;
}
