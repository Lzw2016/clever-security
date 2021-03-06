package org.clever.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜单-API权限表(MenuApi)实体类
 *
 * @author lizw
 * @since 2021-03-04 15:52:53
 */
@Data
public class MenuApi implements Serializable {
    private static final long serialVersionUID = 409939559058448311L;
    /**
     * 菜单id(menu_permission.id)
     */
    @TableId(type = IdType.INPUT)
    private Long menuId;

    /**
     * api id(api_permission.id)
     */
    private Long apiId;

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
