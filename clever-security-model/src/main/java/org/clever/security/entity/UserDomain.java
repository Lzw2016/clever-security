package org.clever.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户-域(UserDomain)实体类
 *
 * @author lizw
 * @since 2020-11-28 19:47:42
 */
@Data
public class UserDomain implements Serializable {
    private static final long serialVersionUID = 740436013600206507L;
    /**
     * 域id
     */
    @TableId(type = IdType.INPUT)
    private Long domainId;

    /**
     * 用户id
     */
    // @TableId(type = IdType.INPUT)
    private String uid;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;
}