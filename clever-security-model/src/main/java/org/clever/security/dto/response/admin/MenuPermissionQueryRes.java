package org.clever.security.dto.response.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.QueryByPage;

import java.util.Date;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:55 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuPermissionQueryRes extends QueryByPage {
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
    private Integer menuSort;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;
    /**
     * 标题
     */
    private String title;
    /**
     * 是否启用授权
     */
    private Integer enabled;
    /**
     * 描述
     */
    private String description;
    /**
     * 域名称
     */
    private String domainName;
}