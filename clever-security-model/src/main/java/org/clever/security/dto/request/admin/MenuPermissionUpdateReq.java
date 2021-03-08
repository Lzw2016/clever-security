package org.clever.security.dto.request.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.clever.common.validation.ValidIntegerStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:55 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuPermissionUpdateReq extends BaseRequest {
    /**
     * 菜单权限id(系统自动生成且不会变化)
     */
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 域id
     */
    @NotNull(message = "域id不能为空")
    private Long domainId;

    /**
     * 上级菜单id
     */
    private Long parentId;

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
    @ValidIntegerStatus(value = {0, 1}, message = "请选择有效选项")
    private Integer hideMenu;

    /**
     * 隐藏子菜单，0:不隐藏(显示)，1:隐藏
     */
    @ValidIntegerStatus(value = {0, 1}, message = "请选择有效选项")
    private Integer hideChildrenMenu;

    /**
     * 菜单扩展配置
     */
    private String extConfig;

    /**
     * 菜单排序
     */
    private Integer sort;

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