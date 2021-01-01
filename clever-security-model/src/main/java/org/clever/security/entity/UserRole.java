package org.clever.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户-角色(UserRole)实体类
 *
 * @author lizw
 * @since 2020-11-28 19:47:43
 */
@Data
public class UserRole implements Serializable {
    private static final long serialVersionUID = 257179787044226368L;
    /**
     * 用户id
     */
    @TableId(type = IdType.INPUT)
    private String uid;

    /**
     * 角色id
     */
    // @TableId(type = IdType.INPUT)
    private Long roleId;

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