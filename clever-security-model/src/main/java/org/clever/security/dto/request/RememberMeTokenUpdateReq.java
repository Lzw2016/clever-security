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
 * 创建时间：2018-09-24 16:58 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RememberMeTokenUpdateReq extends BaseRequest {

    @NotBlank
    @Length(max = 63)
    @ApiModelProperty("token数据")
    private String token;

    @NotNull
    @ApiModelProperty("最后使用时间")
    private Date lastUsed;
}
