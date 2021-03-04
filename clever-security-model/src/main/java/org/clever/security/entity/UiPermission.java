package org.clever.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * UI组件权限表(permission子表)(UiPermission)实体类
 *
 * @author lizw
 * @since 2021-03-04 15:35:34
 */
@Data
public class UiPermission implements Serializable {
    private static final long serialVersionUID = 610515485220015603L;
    /**
     * 页面ui id(系统自动生成且不会变化)
     */
    @TableId(type = IdType.INPUT)
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
    private Long menuPermissionId;

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
}
