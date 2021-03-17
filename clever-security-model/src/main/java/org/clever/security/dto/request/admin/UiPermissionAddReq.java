package org.clever.security.dto.request.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.clever.common.validation.ValidIntegerStatus;
import org.clever.security.entity.EnumConstant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:55 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UiPermissionAddReq extends BaseRequest {
    /**
     * 所属菜单id
     */
    @NotNull(message = "所属菜单id不能为空")
    private Long menuId;
    /**
     * ui组件名
     */
    @NotBlank(message = "ui组件名不能为空")
    private String uiName;
    /**
     * 是否启用授权，0:不启用，1:启用
     */
    @NotNull(message = "是否启用不能为空")
    @ValidIntegerStatus(
            value = {EnumConstant.Permission_Enabled_0, EnumConstant.Permission_Enabled_1},
            message = "是否启用值无效"
    )
    private Integer enabled;
    /**
     * 权限说明
     */
    private String description;
}