package org.clever.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据域(Domain)实体类
 *
 * @author lizw
 * @since 2020-11-28 19:47:38
 */
@Data
public class Domain implements Serializable {
    private static final long serialVersionUID = 180035182024883508L;
    /**
     * 域id(系统自动生成且不会变化)
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 域名称
     */
    private String name;

    /**
     * Redis前缀
     */
    private String redisNameSpace;

    /**
     * 说明
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