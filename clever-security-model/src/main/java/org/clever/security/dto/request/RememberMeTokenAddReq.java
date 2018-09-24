package org.clever.security.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-24 16:39 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RememberMeTokenAddReq extends BaseRequest {

    @NotBlank
    @Length(max = 63)
    @ApiModelProperty("token序列号")
    private String series;

    @NotBlank
    @Length(max = 63)
    @ApiModelProperty("用户登录名")
    private String username;

    @NotBlank
    @Length(max = 63)
    @ApiModelProperty("token数据")
    private String token;

    @NotNull
    @ApiModelProperty("最后使用时间")
    private Date lastUsed;
}
