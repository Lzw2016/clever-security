package org.clever.security.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 服务之间访问Token表(ServerAccessToken)实体类
 *
 * @author lizw
 * @since 2021-01-23 17:09:57
 */
@Data
public class ServerAccessToken implements Serializable {
    private static final long serialVersionUID = -67736960555985068L;
    /**
     * Token id(系统自动生成且不会变化)
     */
    private Long id;

    /**
     * 域id
     */
    private Long domainId;

    /**
     * Token标签
     */
    private String tag;

    /**
     * Token名称
     */
    private String tokenName;

    /**
     * Token值
     */
    private String tokenValue;

    /**
     * Token过期时间(空表示永不过期)
     */
    private Date expiredTime;

    /**
     * Token是否禁用，0:未禁用；1:已禁用
     */
    private Integer disable;

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