package org.clever.security.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;
import org.clever.security.entity.model.WebPermissionModel;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-24 20:45 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WebPermissionInitRes extends BaseResponse {

    @ApiModelProperty("新增的权限")
    private List<WebPermissionModel> addPermission;

    @ApiModelProperty("数据库里存在，系统中不存在的权限")
    private List<WebPermissionModel> notExistPermission;
}
