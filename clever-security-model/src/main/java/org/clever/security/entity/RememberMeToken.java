package org.clever.security.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * “记住我”功能的token(RememberMeToken)实体类
 *
 * @author lizw
 * @since 2020-11-28 19:47:40
 */
@Data
public class RememberMeToken implements Serializable {
    private static final long serialVersionUID = -62158371495135117L;
    /**
     * 记住我Token id(系统自动生成且不会变化)
     */
    private Long id;

    /**
     * 域id
     */
    private Long domainId;

    /**
     * token序列号
     */
    private String series;

    /**
     * 用户id
     */
    private String uid;

    /**
     * token数据
     */
    private String token;

    /**
     * 最后使用时间
     */
    private Date lastUsed;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;
}