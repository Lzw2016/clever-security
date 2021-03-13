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
public class MenuPermissionAddReq extends BaseRequest {
    /**
     * 域id
     */
    @NotNull(message = "域id不能为空")
    private Long domainId;

    /**
     * 上级菜单id
     */
    private Long parentId = -1L;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    private String name;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 菜单路径
     */
    @NotBlank(message = "菜单路径不能为空")
    private String path;

    /**
     * 页面路径
     */
    private String pagePath;

    /**
     * 隐藏当前菜单和子菜单，0:不隐藏(显示)，1:隐藏
     */
    @NotNull(message = "隐藏当前菜单不能为空")
    @ValidIntegerStatus(value = {EnumConstant.MenuPermission_Hide_0, EnumConstant.MenuPermission_Hide_1}, message = "隐藏当前菜单值无效")
    private Integer hideMenu;

    /**
     * 隐藏子菜单，0:不隐藏(显示)，1:隐藏
     */
    @NotNull(message = "隐藏子菜单不能为空")
    @ValidIntegerStatus(value = {EnumConstant.MenuPermission_Hide_0, EnumConstant.MenuPermission_Hide_1}, message = "隐藏子菜单值无效")
    private Integer hideChildrenMenu;

    /**
     * 菜单排序
     */
    @NotNull(message = "菜单排序不能为空")
    private Integer sort;

    /**
     * 是否启用授权，0:不启用，1:启用
     */
    @NotNull(message = "是否启用不能为空")
    @ValidIntegerStatus(value = {EnumConstant.Permission_Enabled_0, EnumConstant.Permission_Enabled_1}, message = "是否启用授权值无效")
    private Integer enabled;

    /**
     * 权限说明
     */
    private String description;
}