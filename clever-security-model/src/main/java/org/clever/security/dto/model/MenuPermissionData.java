package org.clever.security.dto.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/03/15 22:05 <br/>
 */
@Data
public class MenuPermissionData implements Serializable {
    // --------------------------------------------------------------------------------------------------------- ApiPermission

    /**
     * 菜单id(系统自动生成且不会变化)
     */
    private Long id;

    /**
     * 域id
     */
    private Long domainId;

    /**
     * 权限id
     */
    private Long permissionId;

    /**
     * 上级菜单id
     */
    private Long parentId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 页面路径
     */
    private String pagePath;

    /**
     * 隐藏当前菜单和子菜单，0:不隐藏(显示)，1:隐藏
     */
    private Integer hideMenu;

    /**
     * 隐藏子菜单，0:不隐藏(显示)，1:隐藏
     */
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
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

    // --------------------------------------------------------------------------------------------------------- permission

    /**
     * 权限唯一字符串标识
     */
    private String strFlag;

    /**
     * 权限类型，1:API权限，2:菜单权限，3:页面UI权限
     */
    private Integer permissionType;

    /**
     * 是否启用授权，0:不启用，1:启用
     */
    private Integer enabled;

    /**
     * 权限说明
     */
    private String description;

    // --------------------------------------------------------------------------------------------------------- domain

    /**
     * 域名称
     */
    private String domainName;
}
