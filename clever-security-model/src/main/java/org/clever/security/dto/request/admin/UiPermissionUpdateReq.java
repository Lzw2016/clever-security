package org.clever.security.dto.request.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.QueryByPage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:55 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UiPermissionUpdateReq extends QueryByPage {
    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Long id;
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
    private Integer enabled;
    /**
     * 权限说明
     */
    private String description;
}