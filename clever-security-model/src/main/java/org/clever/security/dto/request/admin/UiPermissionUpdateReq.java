package org.clever.security.dto.request.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.QueryByPage;
import org.clever.common.validation.StringNotBlank;
import org.clever.common.validation.ValidIntegerStatus;
import org.clever.security.entity.EnumConstant;

import javax.validation.constraints.NotNull;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:55 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UiPermissionUpdateReq extends QueryByPage {
    /**
     * 页面ui id(系统自动生成且不会变化)
     */
    @NotNull(message = "id不能为空")
    private Long id;
    /**
     * ui组件名
     */
    @StringNotBlank(message = "ui组件名不能为空")
    private String uiName;
    /**
     * 是否启用授权，0:不启用，1:启用
     */
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