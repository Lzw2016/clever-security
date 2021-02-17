package org.clever.security.dto.response.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/17 12:53 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServerAccessTokenQueryRes extends BaseResponse {
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

    // --------------------------------------------------------------------------------------------------------- domain

    /**
     * 域名称
     */
    private String domainName;
}
