package org.clever.security.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * UI组件权限表(permission子表)(UiPermission)实体类
 *
 * @author lizw
 * @since 2020-11-28 19:47:41
 */
@Data
public class UiPermission implements Serializable {
    private static final long serialVersionUID = -56300576206447968L;
    /**
     * ui组件权限id(系统自动生成且不会变化)
     */
    private Long id;

    /**
     * 权限id
     */
    private Long permissionId;

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