package org.clever.security.dto.response.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

import java.util.Date;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:55 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UiPermissionQueryRes extends BaseResponse {
    // --------------------------------------------------------------------------------------------------------- UiPermission
    /**
     * 页面ui id(系统自动生成且不会变化)
     */
    private Long id;

    /**
     * 域id
     */
    private Long domainId;

    /**
     * 权限id
     */
    private Long permissionId;

    /**
     * 所属菜单id
     */
    private Long menuId;

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

    // --------------------------------------------------------------------------------------------------------- permission

    /**
     * 权限唯一字符串标识
     */
    private String strFlag;

    /**
     * 权限类型，1:API权限，2:菜单权限，3:页面UI权限
     */
    private Integer permissionType;

    /**
     * 是否启用授权，0:不启用，1:启用
     */
    private Integer enabled;

    /**
     * 权限说明
     */
    private String description;

    // --------------------------------------------------------------------------------------------------------- domain

    /**
     * 域名称
     */
    private String domainName;
}