package org.clever.security.dto.response.admin;


import lombok.Data;
import org.clever.common.utils.tree.ITreeNode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/03/12 21:32 <br/>
 */
@Data
public class MenuPermissionTreeRes implements ITreeNode {
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

    // --------------------------------------------------------------------------------------------------------- ITreeNode

    /**
     * 子节点
     */
    private List<ITreeNode> children;

    /**
     * 是否被添加到父节点下
     */
    private boolean isBuild = false;

    @Override
    public List<? extends ITreeNode> getChildren() {
        return children;
    }

    @Override
    public void addChildren(ITreeNode node) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(node);
    }

    @Override
    public Boolean isRoot() {
        return parentId == null || Objects.equals(parentId, -1L);
    }
}
