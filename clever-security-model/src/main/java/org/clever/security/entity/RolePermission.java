package org.clever.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
    @TableId(type = IdType.INPUT)
    private Long roleId;

    /**
     * 权限id
     */
    // @TableId(type = IdType.INPUT)
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