package org.clever.security.dto.response.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;
import org.clever.common.utils.tree.ITreeNode;
import org.clever.security.dto.model.MenuPermissionData;
import org.clever.security.dto.model.UiPermissionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/03/12 21:32 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuAndUIPermissionTreeRes extends BaseResponse implements ITreeNode {
    // --------------------------------------------------------------------------------------------------------- MenuPermission UiPermission

    private Long menuId;

    private Long uiId;

    /**
     * 菜单数据
     */
    private MenuPermissionData menuPermission;

    /**
     * UI数据
     */
    private UiPermissionData uiPermission;

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
    public Object getId() {
        if (menuPermission != null && menuPermission.getId() != null) {
            return String.format("menu-%s", menuPermission.getId());
        }
        if (uiPermission != null && uiPermission.getId() != null) {
            return String.format("ui-%s", uiPermission.getId());
        }
        return null;
    }

    @Override
    public Object getParentId() {
        if (menuPermission != null && menuPermission.getParentId() != null) {
            return String.format("menu-%s", menuPermission.getParentId());
        }
        if (uiPermission != null && uiPermission.getMenuId() != null) {
            return String.format("menu-%s", uiPermission.getMenuId());
        }
        return null;
    }

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
        return menuPermission != null && (menuPermission.getId() == null || Objects.equals(menuPermission.getId(), -1L));
    }
}
