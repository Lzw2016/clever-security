package org.clever.security.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜单权限表(permission子表)(MenuPermission)实体类
 *
 * @author lizw
 * @since 2021-02-14 21:17:06
 */
@Data
public class MenuPermission implements Serializable {
    private static final long serialVersionUID = -81883307472903045L;
    /**
     * 菜单权限id(系统自动生成且不会变化)
     */
    private Long id;

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

}
