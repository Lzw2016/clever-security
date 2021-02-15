package org.clever.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 权限表(Permission)实体类
 *
 * @author lizw
 * @since 2020-11-28 19:47:40
 */
@Data
public class Permission implements Serializable {
    private static final long serialVersionUID = -50077799628660347L;
    /**
     * 权限id(系统自动生成且不会变化)
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 上级权限id
     */
    private Long parentId;

    /**
     * 域id
     */
    private Long domainId;

    /**
     * 权限唯一字符串标识
     */
    private String strFlag;

    /**
     * 权限标题
     */
    private String title;

    /**
     * 权限类型，1:API权限, 2:菜单权限，3:UI组件权限
     */
    private Integer resourcesType;

    /**
     * 是否启用授权，0:不启用，1:启用
     */
    private Integer enabled;

    /**
     * 权限说明
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;
}