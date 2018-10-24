package org.clever.security.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-24 10:28 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserAuthenticationReq extends BaseRequest {

    public static final String LoginType_Username = "username";
    public static final String LoginType_Telephone = "telephone";

    @ApiModelProperty("用户登录名(登录名、手机、邮箱)")
    @NotBlank
    private String loginName;

    @ApiModelProperty("登录方式(username、telephone)")
    @Pattern(regexp = "username|telephone")
    private String loginType = "username";

    @ApiModelProperty("验证密码(使用AES对称加密)")
    @NotBlank
    private String password;

    @ApiModelProperty("系统名称(选填)")
    private String sysName;
}
