package org.clever.security.dto.model;

import lombok.Data;
import org.clever.common.utils.tree.ITreeNode;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/03/15 22:05 <br/>
 */
@Data
public class UiPermissionData implements ITreeNode, Serializable {
    // --------------------------------------------------------------------------------------------------------- UiPermission
    /**
     * 页面ui id(系统自动生成且不会变化)
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
     * 所属菜单id
     */
    private Long menuId;

    /**
     * 页面UI组件名称
     */
    private String uiName;

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

    @Override
    public Object getParentId() {
        return menuId;
    }

    @Override
    public boolean isBuild() {
        return true;
    }

    @Override
    public void setBuild(boolean isBuild) {
    }

    @Override
    public List<? extends ITreeNode> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public void addChildren(ITreeNode node) {
        throw new UnsupportedOperationException("页面UI不支持层级结构");
    }

    @Override
    public Boolean isRoot() {
        return menuId == null || Objects.equals(menuId, -1L);
    }
}
