package org.clever.security.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-24 11:06 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserAuthenticationRes extends BaseResponse {

    @ApiModelProperty("登录是否成功")
    private Boolean success = false;

    @ApiModelProperty("登录失败消息")
    private String failMessage;
}
