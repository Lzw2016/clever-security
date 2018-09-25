package org.clever.security.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-24 19:03 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WebPermissionModelGetReq extends BaseRequest {

    @NotBlank
    @Length(max = 127)
    @ApiModelProperty("系统名称")
    private String sysName;

    @NotBlank
    @Length(max = 255)
    @ApiModelProperty("Controller Class")
    private String controllerClass;

    @NotBlank
    @Length(max = 255)
    @ApiModelProperty("Controller Method")
    private String controllerMethod;

    @NotNull
    @Length(max = 255)
    @ApiModelProperty("Controller Method Params")
    private String controllerMethodParams;
}
