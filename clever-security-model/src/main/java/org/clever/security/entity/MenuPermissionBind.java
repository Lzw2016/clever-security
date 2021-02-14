package org.clever.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜单权限绑定表(MenuPermissionBind)实体类
 *
 * @author lizw
 * @since 2021-02-14 23:10:36
 */
@Data
public class MenuPermissionBind implements Serializable {
    private static final long serialVersionUID = 517218571407649098L;
    /**
     * 菜单id
     */
    @TableId(type = IdType.INPUT)
    private Long menuId;

    /**
     * 权限id(API权限、UI权限)
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
