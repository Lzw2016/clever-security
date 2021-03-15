package org.clever.security.dto.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/03/15 22:25 <br/>
 */
@Data
public class MenuAndUIPermissionData implements Serializable {
    private Long menuId;

    /**
     * 菜单数据
     */
    private MenuPermissionData menuPermission;

    /**
     * UI数据
     */
    private List<UiPermissionData> uiPermissionList;
}
