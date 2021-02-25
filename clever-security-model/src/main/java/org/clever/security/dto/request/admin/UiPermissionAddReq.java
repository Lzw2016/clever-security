package org.clever.security.dto.request.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.QueryByPage;
import org.clever.common.validation.ValidIntegerStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:55 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UiPermissionAddReq extends QueryByPage {
    /**
     * 域id
     */
    @NotNull(message = "域id不能为空")
    private Long domainId;
    /**
     * ui组件名
     */
    @NotBlank(message = "ui组件名不能为空")
    private String uiName;
    /**
     * 权限标题
     */
    private String title;
    /**
     * 是否启用授权，0:不启用，1:启用
     */
    @ValidIntegerStatus(value = {0, 1}, message = "请选择有效选项")
    private Integer enabled;
    /**
     * 权限说明
     */
    private String description;
}