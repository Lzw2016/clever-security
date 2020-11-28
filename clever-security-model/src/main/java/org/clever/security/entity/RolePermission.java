package org.clever.security.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色-权限(RolePermission)实体类
 *
 * @author lizw
 * @since 2020-11-28 19:47:41
 */
@Data
public class RolePermission implements Serializable {
    private static final long serialVersionUID = -52342937828874056L;
    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 权限id
     */
    private Long permissionId;

    /**
     * 域id
     */
    private Long domainId;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;
}