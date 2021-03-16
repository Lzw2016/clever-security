package org.clever.security.dto.response.admin;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;
import org.clever.common.utils.tree.ITreeNode;
import org.clever.security.dto.model.MenuPermissionData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/03/15 22:25 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuAndUiPermissionTreeRes extends BaseResponse implements ITreeNode, Serializable {
    private Long menuId;

    /**
     * 菜单数据
     */
    @JsonUnwrapped
    private MenuPermissionData menuPermission;

    // /**
    //  * UI数据 --> children
    //  */
    // private List<UiPermissionData> uiPermissionList;

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
            return menuPermission.getId();
        }
        return null;
    }

    @Override
    public Object getParentId() {
        if (menuPermission != null && menuPermission.getParentId() != null) {
            return menuPermission.getParentId();
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
        return menuPermission != null && (menuPermission.getParentId() == null || Objects.equals(menuPermission.getParentId(), -1L));
    }
}
