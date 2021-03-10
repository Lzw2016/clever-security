package org.clever.security.dto.response.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.QueryByPage;

import java.util.Date;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:55 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleQueryRes extends QueryByPage {
    /**
     * id
     */
    private Long id;

    /**
     * 域id
     */
    private Long domainId;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 是否启用，0:不启用，1:启用
     */
    private Integer enabled;

    /**
     * 角色说明
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