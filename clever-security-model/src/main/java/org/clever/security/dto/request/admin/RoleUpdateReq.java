package org.clever.security.dto.request.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.clever.common.validation.ValidIntegerStatus;
import org.clever.security.entity.EnumConstant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/16 17:05 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleUpdateReq extends BaseRequest {
    /**
     * 域id
     */
    @NotNull(message = "域id不能为空")
    private Long domainId;

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 角色名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 是否启用，0:不启用，1:启用
     */
    @ValidIntegerStatus(
            value = {EnumConstant.Role_Enabled_0, EnumConstant.Role_Enabled_1},
            message = "是否启用值无效"
    )
    private Integer enabled;

    /**
     * 角色说明
     */
    private String description;
}
