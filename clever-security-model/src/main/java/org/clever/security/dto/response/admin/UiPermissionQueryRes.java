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
public class UiPermissionQueryRes extends QueryByPage {
    /**
     * id
     */
    private Long id;
    /**
     * 权限id
     */
    private Long permissionId;
    /**
     * 页面UI组件名称
     */
    private String uiName;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;
    /**
     * 标题
     */
    private String title;
    /**
     * 是否启用授权
     */
    private Integer enabled;
    /**
     * 描述
     */
    private String description;
    /**
     * 域名称
     */
    private String domainName;
}