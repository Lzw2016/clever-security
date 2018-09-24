package org.clever.security.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.clever.security.entity.model.WebPermissionModel;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-24 20:36 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WebPermissionInitReq extends BaseRequest {

    @NotNull
    @ApiModelProperty("当前系统所有权限")
    private List<WebPermissionModel> allPermission;
}
