package org.clever.security.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜单权限表(permission子表)(MenuPermission)实体类
 *
 * @author lizw
 * @since 2020-11-28 19:47:39
 */
@Data
public class MenuPermission implements Serializable {
    private static final long serialVersionUID = -79005531404264847L;
    /**
     * 菜单权限id(系统自动生成且不会变化)
     */
    private Long id;

    /**
     * 权限id
     */
    private Long permissionId;

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 菜单隐藏模式，0：不隐藏；1：隐藏当前菜单和子菜单，2:隐藏子菜单
     */
    private Integer hideMode;

    /**
     * 菜单扩展配置
     */
    private String extConfig;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;
}